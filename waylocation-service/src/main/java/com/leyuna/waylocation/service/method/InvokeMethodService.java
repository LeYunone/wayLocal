package com.leyuna.waylocation.service.method;

import com.leyuna.waylocation.response.DataResponse;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * @author pengli
 * @create 2022-02-23 15:01
 * 调用方法服务
 */
@Service
public class InvokeMethodService {

    public DataResponse invokeMethod(Method method,String value){

        //将json格式解析成入参

        //执行方法


        return DataResponse.buildSuccess();
    }
}
