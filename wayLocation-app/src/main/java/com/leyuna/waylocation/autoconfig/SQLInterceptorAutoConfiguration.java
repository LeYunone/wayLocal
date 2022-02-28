package com.leyuna.waylocation.autoconfig;

import com.leyuna.Interceptor.SQLInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author cocowwy.cn
 * @create 2022-02-02-13:48
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@Lazy(false)
public class SQLInterceptorAutoConfiguration implements InitializingBean {

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public SQLInterceptorAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }


    public void afterPropertiesSet() throws Exception {
        SQLInterceptor interceptor = new SQLInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(interceptor);
        }
    }
}
