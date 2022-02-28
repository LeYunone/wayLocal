package com.leyuna.waylocation.control;

import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.param.ParamService;
import com.leyuna.waylocation.service.way.LocationMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * @author pengli
 * @create 2022-02-28 10:32
 * 方法相关控制器
 */
@RestController
@RequestMapping("/method")
public class MethodControl {

    @Autowired
    private LocationMethodService methodService;

    @Autowired
    private ParamService paramService;

    /**
     * 获得方法入参结构
     * @param methodInfo 方法具体信息
     * @return
     */
    @RequestMapping("/getParam")
    public DataResponse getParam(MethodInfoDTO methodInfo){
        //获得方法
        DataResponse<Method> method = methodService.getMethod(methodInfo);
        Method data = method.getData();
        return paramService.getParam(data);
    }

    /**
     * 获得方法出参结构
     * @param methodInfo
     * @return
     */
    @RequestMapping("/getReturnParam")
    public DataResponse getReturnParam(MethodInfoDTO methodInfo){
        DataResponse<Method> method = methodService.getMethod(methodInfo);
        Method data = method.getData();
        return paramService.getReturnParam(data);
    }
}
