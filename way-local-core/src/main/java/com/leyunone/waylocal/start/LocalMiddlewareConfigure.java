package com.leyunone.waylocal.start;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.waylocal.bean.CustomStartInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Lazy;
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
@Lazy(false)
public class LocalMiddlewareConfigure implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if(ObjectUtil.isNull(LocalEasyStartConstants.customStartInfo)) return;
        CustomStartInfo customStartInfo = LocalEasyStartConstants.customStartInfo;
        environment.getPropertySources().addLast(new PropertySource<Object>("") {
            @Override
            public Object getProperty(String name) {
                if(StringUtils.isNotBlank(customStartInfo.getMysqlIp())){
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
        properties.setProperty("spring.main.lazy-initialization", "true");
        PropertiesPropertySource propertySource = new PropertiesPropertySource("myPropertySource", properties);

        // 将新的属性源添加到属性源列表的首位，确保优先级最高
        propertySources.addFirst(propertySource);
    }
}
