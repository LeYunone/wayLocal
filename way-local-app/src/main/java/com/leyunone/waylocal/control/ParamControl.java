package com.leyunone.waylocal.control;

import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.response.DataResponse;
import com.leyunone.waylocal.service.LocationService;
import com.leyunone.waylocal.service.impl.param.ParamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author leyunone
 * @create 2022-02-28 10:32
 * 参数相关控制器
 */
@RestController
@RequestMapping("/param")
public class ParamControl {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ParamServiceImpl paramServiceImpl;

    /**
     * 获得方法入参结构
     *
     * @param methodInfo 方法具体信息
     * @return
     */
    @PostMapping("/getParam")
    public DataResponse getParam (@RequestBody MethodInfoDTO methodInfo) {
        return paramServiceImpl.getParam(methodInfo.getParams());
    }

    /**
     * 获得方法出参结构
     *
     * @param methodInfo
     * @return
     */
    @PostMapping("/getReturnParam")
    public DataResponse getReturnParam (@RequestBody MethodInfoDTO methodInfo) {
        return paramServiceImpl.getReturnParam(methodInfo.getReturnParams());
    }
}
