package com.leyuna.waylocation.control;

import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.LocationMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengli
 * @create 2022-03-02 14:45
 * 定位控制器
 */
@RestController
@RequestMapping("/location")
public class LocationControl {

    @Autowired
    private LocationMethodService locationMethodService;

    @RequestMapping("/getMethodName")
    public DataResponse getMethodName(String methodName,@RequestParam(required = false) Integer size){
        DataResponse method = locationMethodService.getMethod(methodName, size);
        return method;
    }
}
