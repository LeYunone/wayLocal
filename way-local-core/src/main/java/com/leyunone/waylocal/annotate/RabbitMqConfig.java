package com.leyunone.waylocal.annotate;

import java.lang.annotation.*;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RabbitMqConfig {

    String url() default "127.0.0.1:5672";

    String account() default "guest";

    String password() default "guest";

    KeyValue[] keyVales() default {};
}
