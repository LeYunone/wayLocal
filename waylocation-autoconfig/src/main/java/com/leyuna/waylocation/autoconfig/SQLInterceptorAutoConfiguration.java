//package com.leyuna.waylocation.autoconfig;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//
//import java.util.List;
//
///**
// * @author cocowwy.cn
// * @create 2022-02-02-13:48
// */
//@Configuration
//@ConditionalOnBean(SqlSessionFactory.class)
//@AutoConfigureAfter(MybatisAutoConfiguration.class)
//@Lazy(false)
//public class SQLInterceptorAutoConfiguration implements InitializingBean {
//
//    private final List<SqlSessionFactory> sqlSessionFactoryList;
//
//    @Autowired(required = false)
//    public SQLInterceptorAutoConfiguration (List<SqlSessionFactory> sqlSessionFactoryList) {
//        this.sqlSessionFactoryList = sqlSessionFactoryList;
//    }
//
//
//    @Override
//    public void afterPropertiesSet () throws Exception {
////        DataScopeInterceptor interceptor = new DataScopeInterceptor();
////        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
////            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
////            configuration.addInterceptor(interceptor);
////        }
//    }
//}
