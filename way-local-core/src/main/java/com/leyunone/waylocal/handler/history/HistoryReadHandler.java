package com.leyunone.waylocal.handler.history;

import com.leyunone.waylocal.handler.StrategyAutoRegisterComponent;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
public abstract class HistoryReadHandler<R,P> extends StrategyAutoRegisterComponent implements HistoryHandler<R,P> {

    public void save(P p){}
}
