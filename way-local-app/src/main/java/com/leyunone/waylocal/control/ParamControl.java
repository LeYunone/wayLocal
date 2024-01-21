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

import java.util.List;


/**
 * @author leyunone
 * @create 2022-02-28 10:32
 * 参数相关控制器
 */
@RestController
@RequestMapping("/param")
public class ParamControl {

    @Autowired
    private ParamServiceImpl paramServiceImpl;

    /**
     * 获得方法入参结构
     *
     * @param methodInfo 方法具体信息
     * @return
     */
    @PostMapping("/getParam")
    public DataResponse<List<String>> getParam(@RequestBody MethodInfoDTO methodInfo) {
        List<String> param = paramServiceImpl.getParam(methodInfo.getParams());
        return DataResponse.of(param);
    }

    /**
     * 获得方法出参结构
     *
     * @param methodInfo
     * @return
     */
    @PostMapping("/getReturnParam")
    public DataResponse<String> getReturnParam(@RequestBody MethodInfoDTO methodInfo) {
        String returnParam = paramServiceImpl.getReturnParam(methodInfo.getReturnParams());
        return DataResponse.of(returnParam);
    }
}
