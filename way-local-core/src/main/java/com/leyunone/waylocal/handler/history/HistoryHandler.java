package com.leyunone.waylocal.handler.history;

import java.util.List;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
public interface HistoryHandler<R,P> {

    /**
     * 保存方法
     */
    void save(P p);

    /**
     * 读取方法
     *
     * @return
     */
    List<R> get();
}
