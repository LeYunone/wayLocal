package com.leyuna.waylocation.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author pengli
 * @create 2022-04-08 10:39
 */
@ConfigurationProperties(prefix ="waylocation")
public class WayLocationProperties {

    private String saveType;
    
    private String savePath;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSaveType () {
        return saveType;
    }

    public WayLocationProperties setSaveType (String saveType) {
        this.saveType = saveType;
        return this;
    }
}
