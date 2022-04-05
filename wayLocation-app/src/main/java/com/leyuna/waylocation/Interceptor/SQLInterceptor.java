package com.leyuna.waylocation.Interceptor;

import com.leyuna.waylocation.bean.dto.SqlInvokeDTO;
import com.leyuna.waylocation.constant.global.SqlInvokeConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author cocowwy.cn   
 * @create 2022-02-02-14:04
 * 
 *  MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class
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
@Slf4j
public class SQLInterceptor implements Interceptor {
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取本次方法 sql监听目录 记录四个维度
        SqlInvokeDTO invokeInfo = getInvokeInfo();
        Integer goNum = invokeInfo.getGoNum();
        log.info("Sql监听，当前目录："+goNum);
        //sql语句
        List<String> sql = invokeInfo.getSql();
        //sql条件
        List<String> sqlCondition = invokeInfo.getSqlCondition();
        //sql操作
        List<String> sqlAction = invokeInfo.getSqlAction();
        //涉及数据
        List<String> sqlData = invokeInfo.getSqlData();
        //涉及表
        List<String> sqlTable = invokeInfo.getSqlTable();

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
        //获取sql语句
        String strSql = getSql(configuration, boundSql);

        //记录语句
        sql.add(strSql);
        //sql条件
        if(sqlCommandType == SqlCommandType.SELECT){
            //如果是查询则记录条件
            sqlCondition.add(strSql.substring(strSql.indexOf("WHERE")+5));
        }else{
            sqlCondition.add("");
        }
        //sql操作类型
        sqlAction.add(getSqlAction(sqlCommandType));

        return invocation.proceed();
    }

    private String getSqlAction(SqlCommandType type){
        switch (type){
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

    private SqlInvokeDTO getInvokeInfo () {
        SqlInvokeDTO sqlInvokeDTO = null;
        if (SqlInvokeConstant.sqlInvokeDTO == null) {
            sqlInvokeDTO = new SqlInvokeDTO();
            //初始化监听目录
            sqlInvokeDTO.setGoNum(1);
        } else {
            sqlInvokeDTO = SqlInvokeConstant.sqlInvokeDTO;
            //开启本次方法sql监听
            sqlInvokeDTO.setGoNum(sqlInvokeDTO.getGoNum()+1);
        }
        //记录本次监听
        SqlInvokeConstant.sqlInvokeDTO = sqlInvokeDTO;
        return sqlInvokeDTO;
    }

    private String getSql(Configuration configuration, BoundSql boundSql) {
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

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
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
}
