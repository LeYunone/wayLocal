package com.leyunone.waylocal.annotate;

import java.lang.annotation.*;

/**
 * :)
 * 提供本地快速启动的转配逻辑
 *
 * @Author leyunone
 * @Date 2024/1/4 9:46
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocalEasyStart {

    MySqlConfig mysql() default @MySqlConfig;

    RabbitMqConfig rabbitmq() default @RabbitMqConfig;
}
