package com.leyunone.waylocal.bean;

import com.leyunone.waylocal.annotate.LocalEasyStart;
import com.leyunone.waylocal.annotate.MySqlConfig;
import com.leyunone.waylocal.annotate.RabbitMqConfig;
import lombok.*;

/**
 * :)
 * 启动信息
 *
 * @Author leyunone
 * @Date 2024/1/4 10:52
 */
@Getter
@Setter
public class CustomStartInfo {

    private MySqlConfig mySqlConfig;

    private RabbitMqConfig rabbitMqConfig;

    public static CustomStartInfo build(LocalEasyStart localEasyStart) {
        CustomStartInfo customStartInfo = new CustomStartInfo();
        customStartInfo.setMySqlConfig(localEasyStart.mysql());
        customStartInfo.setRabbitMqConfig(localEasyStart.rabbitmq());
        return customStartInfo;
    }
}
