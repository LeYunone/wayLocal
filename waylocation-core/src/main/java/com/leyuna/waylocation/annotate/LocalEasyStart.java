package com.leyuna.waylocation.annotate;

import java.lang.annotation.*;

/**
 * :)
 * 提供本地快速启动的转配逻辑
 *
 * @Author pengli
 * @Date 2024/1/4 9:46
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocalEasyStart {

    boolean MySql() default false;

    boolean Nacos() default false;

    boolean RabbitMq() default false;
}
