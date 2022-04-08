package com.leyuna.waylocation.Interceptor;

import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.constant.global.SqlInvokeConstant;
import com.leyuna.waylocation.dto.SqlInvokeDTO;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author cocowwy.cn
 * @create 2022-02-02-14:04
 * <p>
 * JsqlParser 插件
 * <p>
 * MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class
 */
@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                        CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@Component
@ConditionalOnClass(DataSource.class)
@Slf4j
public class SQLInterceptor implements Interceptor {

    @Override
    public Object intercept (Invocation invocation) throws Throwable {

        //获得本次db操作对应的存储信息
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            //获得sql语句存储信息 包括 条件 语句
            parameter = invocation.getArgs()[1];
        }

        //找到sql
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        //DB配置员
        Configuration configuration = mappedStatement.getConfiguration();
        //操作类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        //获取大写sql语句
        String strSql = getSql(configuration, boundSql).toUpperCase();
        String conditionString = "";
        String dataString = "";
        //操作类型
        String sqlAction = getSqlAction(sqlCommandType);
        //涉及表
        String tableString = StringUtils.join(getSqlTabName(strSql), "/");

        long startTime = System.currentTimeMillis();
        Object result = invocation.proceed();
        long endTime = System.currentTimeMillis();
        //执行时间
        String sqlTime = String.valueOf(endTime - startTime);

        if (sqlCommandType == SqlCommandType.SELECT) {
            //如果是查询则记录条件
            conditionString = strSql.substring(strSql.indexOf("WHERE") + 5);
            //记录结果
            dataString = JSONObject.toJSONString(result);
        } else {
            //操作类型：成功数目
            dataString = getSqlAction(sqlCommandType) + ":" + result;
        }

        //登记db
        registerDB(strSql, conditionString, dataString, sqlAction, tableString, sqlTime);
        return result;
    }

    /**
     * 登记本次db操作
     */
    private void registerDB (String sql, String condition, String data, String action, String tableString, String sqlTime) {
        //获取本次方法 sql监听目录 记录四个维度
        List<SqlInvokeDTO> invokeInfo = getInvokeInfo();
        Integer goNum = invokeInfo.size() + 1;
        log.info("Sql监听，当前目录：" + goNum);
        SqlInvokeDTO invokeDTO = new SqlInvokeDTO();
        invokeDTO.setGoNum(goNum);
        invokeDTO.setSql(sql);
        invokeDTO.setSqlCondition(condition);
        invokeDTO.setSqlData(data);
        invokeDTO.setSqlAction(action);
        invokeDTO.setSqlTable(tableString);
        invokeDTO.setSqlTime(sqlTime);
        invokeInfo.add(invokeDTO);
    }

    private String getSqlAction (SqlCommandType type) {
        switch (type) {
            case INSERT:
                return "INSERT";
            case SELECT:
                return "SELECT";
            case UPDATE:
                return "UPDATE";
            case DELETE:
                return "DELETE";
            case FLUSH:
                return "FLUSH";
            default:
                return "UNKNOWN";
        }
    }

    private List<SqlInvokeDTO> getInvokeInfo () {
        List<SqlInvokeDTO> sqlInvokeDTO = null;
        if (SqlInvokeConstant.sqlInvokeDTO == null) {
            sqlInvokeDTO = new ArrayList<>();
        } else {
            sqlInvokeDTO = SqlInvokeConstant.sqlInvokeDTO;
        }
        //记录本次监听
        SqlInvokeConstant.sqlInvokeDTO = sqlInvokeDTO;
        return sqlInvokeDTO;
    }

    private String getSql (Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterObject == null || parameterMappings.size() == 0) {
            return sql;
        }
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            //如果 ？ 是对应的typeHandler，则说明是拼接参数 #
            sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                //确定本参数是本次sql构造中的参数
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    //翻译参数类型，取代首个？
                    sql = sql.replaceFirst("\\?", getParameterValue(obj));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    //附加参数
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(obj));
                }
            }
        }
        return sql;
    }

    private String getParameterValue (Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof LocalDateTime) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    private List<String> getSqlTabName (String sql) {
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (Exception e) {
        }
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(statement);
        return tableList;
    }
}
