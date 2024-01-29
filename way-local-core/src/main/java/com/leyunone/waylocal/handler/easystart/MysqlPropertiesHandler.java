package com.leyunone.waylocal.handler.easystart;

import com.leyunone.waylocal.bean.CustomStartInfo;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Properties;


/**
 * :)
 * mysql配置
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/30
 */
public class MysqlPropertiesHandler implements PropertiesAgainHandler {

    @Override
    public void enhance(MutablePropertySources propertySources, CustomStartInfo customStartInfo) {
        PropertySource<?> propertySource1 = propertySources.get("application.properties");
        Object asd = propertySource1.getProperty("asd");
        Properties properties = new Properties();
        /**
         * 需要只替换已配置Url的ip地址项
         */
        properties.setProperty("spring.datasource.url", "jdbc:mysql://81.68.137.33:3306/blog?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false");
        properties.setProperty("spring.datasource.url.username", "root");
        properties.setProperty("spring.datasource.url.password", "root");
        properties.setProperty("spring.datasource.url.driver-class-name", "com.mysql.jdbc.Driver");
        PropertiesPropertySource propertySource = new PropertiesPropertySource("application.properties", properties);

        propertySources.addFirst(propertySource);
    }
}
