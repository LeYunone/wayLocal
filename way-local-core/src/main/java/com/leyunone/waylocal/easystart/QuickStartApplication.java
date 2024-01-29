package com.leyunone.waylocal.easystart;

import com.leyunone.waylocal.annotate.LocalEasyStart;
import com.leyunone.waylocal.annotate.MySqlConfig;
import com.leyunone.waylocal.bean.CustomStartInfo;
import org.springframework.boot.SpringApplication;

/**
 * 快速启动 - 本地化简易配置
 */
public class QuickStartApplication {

    public static void easyStart(Class<?> applicationClass, String... args) {
        /**
         * 启动前进行最低装配
         * 全局懒加载
         * 剔除不需要的中间件连接
         * 本地化中间件
         */
        LocalEasyStart annotation = applicationClass.getAnnotation(LocalEasyStart.class);
        if (annotation != null) {
            LocalEasyStartConstants.customStartInfo = CustomStartInfo.build(annotation);
        }
        SpringApplication.run(applicationClass, args);
    }


}
