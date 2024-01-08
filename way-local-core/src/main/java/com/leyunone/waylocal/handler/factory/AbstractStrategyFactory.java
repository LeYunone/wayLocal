package com.leyunone.waylocal.handler.factory;

import java.util.Map;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
public abstract class AbstractStrategyFactory<T> {

    public abstract Map<String,T> strategyStore();
}
