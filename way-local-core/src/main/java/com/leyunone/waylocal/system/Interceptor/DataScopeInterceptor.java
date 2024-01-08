package com.leyunone.waylocal.system.Interceptor;//package com.leyunone.waylocal.system.Interceptor;
//
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
//import com.leyunone.waylocal.bean.dto.SqlInvokeDTO;
//import com.leyunone.waylocal.common.SqlInvokeConstant;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.SystemMetaObject;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.util.List;
//import java.util.Properties;
//
///**
// * @author leyunone
// * @create 2022-03-31-14:04
// */
//@Slf4j
//@AllArgsConstructor
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//@Component
//public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {
//
//    @Override
//    public Object intercept (Invocation invocation) throws Throwable {
//
//        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
//        //获取 StatementHandler 包装类
//        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
//        this.sqlParser(metaObject);
//
//        //获取接口的映射信息
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
//            return invocation.proceed();
//        }
//        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
//        // 执行的SQL语句
//        String originalSql = boundSql.getSql();
//
//        return invocation.proceed();
//    }
//
//    private SqlInvokeDTO getInvokeInfo () {
//        SqlInvokeDTO sqlInvokeDTO = null;
//        if (SqlInvokeConstant.sqlInvokeDTO == null) {
//            sqlInvokeDTO = new SqlInvokeDTO();
//            //初始化监听目录
//            sqlInvokeDTO.setGoNum(1);
//        } else {
//            sqlInvokeDTO = SqlInvokeConstant.sqlInvokeDTO;
//            //开启本次方法sql监听
//            sqlInvokeDTO.setGoNum(sqlInvokeDTO.getGoNum()+1);
//        }
//        //记录本次监听
//        SqlInvokeConstant.sqlInvokeDTO = sqlInvokeDTO;
//        return sqlInvokeDTO;
//    }
//
//    /**
//     * 生成拦截对象的代理
//     *
//     * @param target 目标对象
//     * @return 代理对象
//     */
//    @Override
//    public Object plugin (Object target) {
//        if (target instanceof StatementHandler) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }
//
//    /**
//     * mybatis配置的属性
//     *
//     * @param properties mybatis配置的属性
//     */
//    @Override
//    public void setProperties (Properties properties) {
//    }
//}
