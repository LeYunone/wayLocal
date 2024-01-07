package com.leyunone.waylocal.system.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2022-05-09
 */
@ConfigurationProperties(prefix ="spring.datasource")
public class DBProperties {

    private String url;

    private String username;

    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
