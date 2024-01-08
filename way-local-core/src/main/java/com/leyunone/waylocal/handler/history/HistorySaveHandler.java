package com.leyunone.waylocal.handler.history;

import com.leyunone.waylocal.handler.StrategyAutoRegisterComponent;

import java.util.List;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
public abstract class HistorySaveHandler<R,P> extends StrategyAutoRegisterComponent implements HistoryHandler<R,P> {

    public List<R> get(){
        return null;
    }
}
