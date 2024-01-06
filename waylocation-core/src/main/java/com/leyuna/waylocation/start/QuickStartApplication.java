package com.leyuna.waylocation.start;

import com.leyuna.waylocation.annotate.LocalEasyStart;
import com.leyuna.waylocation.bean.CustomStartInfo;
import com.leyuna.waylocation.start.LocalEasyStartConstants;
import org.springframework.boot.SpringApplication;

/**
 * 快速启动 - 简易配置
 */
public class QuickStartApplication {

    public static void easyStart(Class<?> applicationClass) {
        SpringApplication.run(applicationClass);
    }

    public static void easyStart(Class<?> applicationClass, String... args) {
        /**
         * 启动前进行最低装配
         * 全局懒加载
         * 剔除不需要的中间件连接
         * 本地化中间件
         */
        LocalEasyStart annotation = applicationClass.getAnnotation(LocalEasyStart.class);
        if (annotation != null) {
            CustomStartInfo customStartInfo = new CustomStartInfo();
            customStartInfo.setMysqlIp("123");
            LocalEasyStartConstants.customStartInfo = customStartInfo;
        }
        SpringApplication.run(applicationClass, args);
    }


}
