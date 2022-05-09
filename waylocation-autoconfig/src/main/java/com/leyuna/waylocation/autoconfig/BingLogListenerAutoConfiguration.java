package com.leyuna.waylocation.autoconfig;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.leyuna.waylocation.config.BingLogEventListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2022-05-09
 * Binglog日志监听
 */
@Configuration
@EnableConfigurationProperties(DBProperties.class)
public class BingLogListenerAutoConfiguration {

    public BingLogListenerAutoConfiguration(DBProperties dbProperties){
        //TODO 2022/5/9 只监听本机数据库的binlog
        String url = dbProperties.getUrl();

        //加载出bingLog监视器
        BinaryLogClient binaryLogClient = new BinaryLogClient(dbProperties.getUsername(), dbProperties.getPassword());
        binaryLogClient.registerEventListener(new BingLogEventListener());

        try {
            binaryLogClient.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
