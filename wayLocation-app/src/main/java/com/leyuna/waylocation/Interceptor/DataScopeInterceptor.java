package com.leyuna.waylocation.Interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.leyuna.waylocation.bean.dto.SqlInvokeDTO;
import com.leyuna.waylocation.constant.global.SqlInvokeConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * @author pengli
 * @create 2022-03-31-14:04
 */
@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

    @Override
    public Object intercept (Invocation invocation) throws Throwable {
        //获取本次方法 sql监听目录 记录四个维度
        SqlInvokeDTO invokeInfo = getInvokeInfo();
        Integer goNum = invokeInfo.getGoNum();
        log.info("Sql监听，当前目录："+goNum);
        //sql语句
        List<String> sql = invokeInfo.getSql();
        //sql条件
        List<String> sqlCondition = invokeInfo.getSqlCondition();
        //涉及数据
        List<String> sqlData = invokeInfo.getSqlData();
        //涉及表
        List<String> sqlTable = invokeInfo.getSqlTable();


        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作 不是直接过滤
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        sql.add(originalSql);
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();
        Object[] args = invocation.getArgs();

        return invocation.proceed();
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

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin (Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties (Properties properties) {
    }
}
