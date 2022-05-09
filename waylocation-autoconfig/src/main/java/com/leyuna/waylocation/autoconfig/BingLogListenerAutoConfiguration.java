package com.leyuna.waylocation.autoconfig;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.leyuna.waylocation.config.BingLogEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@AutoConfigureBefore(WaylocationAutoConfiguration.class)
public class BingLogListenerAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String userName;

    private String passWord;

    public BingLogListenerAutoConfiguration(DBProperties dbProperties){
        //TODO 2022/5/9 只监听本机数据库的binlog
        String url = dbProperties.getUrl();
        this.userName = dbProperties.getUsername();
        this.passWord = dbProperties.getPassword();
    }

    @Bean(name = "BinaryLogClient")
    public BinaryLogClient getBinaryLogClient(){
        //加载出bingLog监视器
        BinaryLogClient binaryLogClient = new BinaryLogClient(userName,passWord);
        binaryLogClient.registerEventListener(new BingLogEventListener());
        logger.info("加载= = = ==BinaryLogClient");
        return binaryLogClient;
    }
}
