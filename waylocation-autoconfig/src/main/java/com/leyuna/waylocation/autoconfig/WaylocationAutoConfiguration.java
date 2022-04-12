package com.leyuna.waylocation.autoconfig;

import com.leyuna.waylocation.constant.global.ServerConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengli
 * @date 2022-04-07
 */
@Configuration()
@ComponentScan({"com.leyuna.waylocation"})
@EnableConfigurationProperties(WayLocationProperties.class)
public class WaylocationAutoConfiguration {


    public WaylocationAutoConfiguration(WayLocationProperties wayLocationProperties){
        //保存类型，由用户决定保存方式：session cookie file
        String saveType = wayLocationProperties.getSaveType();
        if(StringUtils.isNotBlank(saveType)){
            ServerConstant.saveType = saveType;
        }else{
            //默认为cookie保存
            ServerConstant.saveType = "cookie";
        }
    }
}
