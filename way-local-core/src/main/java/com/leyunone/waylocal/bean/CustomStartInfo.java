package com.leyunone.waylocal.bean;

import lombok.Getter;
import lombok.Setter;

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

    private boolean mysql;

    private boolean nacos;

    private boolean rabbitMq;

    private String mysqlIp;

    private Integer mysqlPort;

    private String nacosIp;

    private Integer nacosPort;

    private String rabbitMqIp;

    private Integer rabbitMqPort;
}
