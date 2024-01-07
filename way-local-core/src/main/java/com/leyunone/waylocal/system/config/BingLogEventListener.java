package com.leyunone.waylocal.system.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2022-05-09
 */
public class BingLogEventListener implements BinaryLogClient.EventListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onEvent(Event event) {
        EventData data = event.getData();
        logger.info("======"+data);
    }
}
