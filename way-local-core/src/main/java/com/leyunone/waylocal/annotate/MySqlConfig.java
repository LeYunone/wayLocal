package com.leyunone.waylocal.annotate;

import java.lang.annotation.*;

/**
 * :)
 * mysql配置
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MySqlConfig {

    String url() default "127.0.0.1:3306";

    String account() default "root";

    String password() default "root";

    KeyValue[] keyVales() default {};
}
