package com.leyunone.waylocal.easystart;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.leyunone.waylocal.bean.CustomStartInfo;
import com.leyunone.waylocal.handler.easystart.PropertiesAgainHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (ObjectUtil.isNull(LocalEasyStartConstants.customStartInfo)) return;
        CustomStartInfo customStartInfo = LocalEasyStartConstants.customStartInfo;
        MutablePropertySources propertySources = environment.getPropertySources();

        /**
         * 走配置增强的所有策略
         */
        String aPackage = ClassUtil.getPackage(PropertiesAgainHandler.class);
        Set<Class<?>> classes = ClassUtil.scanPackage(aPackage);
        Set<Class<?>> strategy = classes.stream().filter(sonClass -> {
            boolean allAssignableFrom = ClassUtil.isAllAssignableFrom(new Class[]{PropertiesAgainHandler.class},
                    new Class[]{sonClass});
            return allAssignableFrom && sonClass != PropertiesAgainHandler.class;
        }).collect(Collectors.toSet());
        /**
         *  方法名为 enhance
         */
        for (Class<?> instance : strategy) {
            Object o1 = ReflectUtil.newInstance(instance);
            ReflectUtil.invoke(o1, "enhance", propertySources, customStartInfo);
        }
    }
}
