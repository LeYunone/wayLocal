package com.leyuna.waylocation.control;

import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    private LocationService locationService;

    @RequestMapping("/getClassName")
    public DataResponse getClassName(String className,@RequestParam(required = false,defaultValue = "10") Integer size){
        if(!StringUtils.isEmpty(className)){
            return locationService.getClassName(className, size);
        }
        return DataResponse.buildSuccess();
    }
}
