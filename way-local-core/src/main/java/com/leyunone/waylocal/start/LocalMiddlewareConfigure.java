package com.leyunone.waylocal.start;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.leyunone.waylocal.bean.CustomStartInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * :)
 * 中间件本地化 配置覆写
 * ip / port
 *
 * @Author leyunone
 * @Date 2024/1/4 10:45
 */
@Component
@Order(0)
public class LocalMiddlewareConfigure implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if(ObjectUtil.isNull(LocalEasyStartConstants.customStartInfo)) return;
        CustomStartInfo customStartInfo = LocalEasyStartConstants.customStartInfo;
        environment.getPropertySources().addLast(new PropertySource<Object>("application.properties") {
            @Override
            public Object getProperty(String name) {
                if(StrUtil.isNotBlank(customStartInfo.getMysqlIp())){
                    if ("spring.datasource.main.jdbc-url".equals(name)) {
                        return "jdbc:mysql://192.168.151.233:3306/pms?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&useSSL=false";
                    }
                }
                return null;
            }
        });

        MutablePropertySources propertySources = environment.getPropertySources();

        // 创建一个新的属性源
        Properties properties = new Properties();
        properties.setProperty("spring.main.lazy-initialization", "false");
        properties.setProperty("spring.datasource.url","jdbc:mysql://81.68.137.33:3306/blog?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false");
        properties.setProperty("spring.datasource.url.username","root");
        properties.setProperty("spring.datasource.url.password","Aa3201360");
        properties.setProperty("spring.datasource.url.driver-class-name","com.mysql.jdbc.Driver");
        PropertiesPropertySource propertySource = new PropertiesPropertySource("application.properties", properties);

        // 将新的属性源添加到属性源列表的首位，确保优先级最高
        propertySources.addFirst(propertySource);
    }
}
