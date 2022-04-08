package com.leyuna.waylocation.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
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

//    @Autowired
//    private WayLocationProperties wayLocationProperties;


    public WaylocationAutoConfiguration(){
        System.out.println("///////////");
    }
}
