package com.leyuna.waylocation.control;

import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.InvokeMethodService;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * @author pengli
 * @create 2022-02-28 14:17
 * 方法相关控制器
 */
@RestController
@RequestMapping("/method")
public class MethodControl {

    @Autowired
    private LocationService locationMethodService;

    @Autowired
    private InvokeMethodService methodService;

    /**
     * 获得历史方法调用记录
     * @return
     */
    @RequestMapping("/getHistory")
    public DataResponse getHistory(){
        return DataResponse.buildSuccess();
    }


    /**
     * 方法调用
     * @param methodInfo
     * @return
     */
    @RequestMapping("/invokeMethod")
    public DataResponse invokeMethod(MethodInfoDTO methodInfo){
        DataResponse<Method> method =
                locationMethodService.getMethod(methodInfo);
        Method data = method.getData();
        return methodService.invokeMethod(data, methodInfo.getParamJsonValue());
    }


}
