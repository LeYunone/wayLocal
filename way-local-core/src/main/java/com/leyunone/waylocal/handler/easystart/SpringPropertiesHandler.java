package com.leyunone.waylocal.handler.easystart;

import com.leyunone.waylocal.bean.CustomStartInfo;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * :)
 * spring配置
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/30
 */
public class SpringPropertiesHandler implements PropertiesAgainHandler {

    @Override
    public void enhance(MutablePropertySources propertySources, CustomStartInfo customStartInfo) {
        // 创建一个新的属性源
        Properties properties = new Properties();
        properties.setProperty("spring.main.lazy-initialization", "true");
        PropertiesPropertySource propertySource = new PropertiesPropertySource("application.properties", properties);
        propertySources.addFirst(propertySource);
    }
}
