package com.leyuna.waylocation.control;

import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.LocationService;
import com.leyuna.waylocation.service.param.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * @author pengli
 * @create 2022-02-28 10:32
 * 参数相关控制器
 */
@RestController
@RequestMapping("/param")
public class ParamControl {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ParamService paramService;

    /**
     * 获得方法入参结构
     * @param methodInfo 方法具体信息
     * @return
     */
    @PostMapping("/getParam")
    public DataResponse getParam(@RequestBody MethodInfoDTO methodInfo){
        return paramService.getParam(methodInfo.getParams());
    }

    /**
     * 获得方法出参结构
     * @param methodInfo
     * @return
     */
    @PostMapping("/getReturnParam")
    public DataResponse getReturnParam(@RequestBody MethodInfoDTO methodInfo){
        return paramService.getReturnParam(methodInfo.getReturnParams());
    }
}
