package com.leyuna.waylocation.service.way;

import com.leyuna.waylocation.response.DataResponse;

import java.lang.reflect.Method;

/**
 * @author pengli
 * @create 2022-02-23 15:01
 * 调用方法服务
 */
public class InvokeMethodService {

    public DataResponse invokeMethod(Method method){

        return DataResponse.buildSuccess();
    }
}
