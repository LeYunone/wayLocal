package com.leyunone.waylocal.handler.factory;

import com.leyunone.waylocal.handler.history.HistoryHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
@Service
public class HistoryHandlerFactory extends AbstractStrategyFactory<HistoryHandler> {

    private ConcurrentHashMap<String,HistoryHandler> map = new ConcurrentHashMap<>();

    @Override
    public Map<String, HistoryHandler> strategyStore() {
        return map;
    }

    public HistoryHandler getStrategyStore(String key) {
        return map.get(key);
    }
}
