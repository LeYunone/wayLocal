package com.leyunone.waylocal.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leyunone
 * @create 2022-04-08 10:39
 */
@ConfigurationProperties(prefix ="waylocal")
public class WaylocalProperties {

    private String saveType;

    private String savePath;

    private Boolean isBingLog;

    public Boolean getBingLog() {
        return isBingLog;
    }

    public void setBingLog(Boolean bingLog) {
        isBingLog = bingLog;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSaveType () {
        return saveType;
    }

    public WaylocalProperties setSaveType (String saveType) {
        this.saveType = saveType;
        return this;
    }
}
