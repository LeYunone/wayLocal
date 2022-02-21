package com.leyuna.waylocation.service.way;

import com.leyuna.waylocation.response.DataResponse;

/**
 * @author pengli
 * @date  2022-02-21 13:59
 * 定位方法接口     [根据方法名+类名组合？ 展示项目内所有条件方法？]
 */
public class LocationWayService {

    /**
     * 获得方法
     * @return DataResponse
     */
    public DataResponse getWay(){

        return DataResponse.buildSuccess();
    }
}
