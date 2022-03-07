package com.leyuna.waylocation.control;

import com.leyuna.waylocation.bean.dto.ClassDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.InvokeMethodService;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

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
    public DataResponse getHistory(@CookieValue("historyMethodInfo") Queue<MethodInfoDTO> historyMethod){
        return DataResponse.buildSuccess();
    }


    /**
     * 方法调用
     * @param methodInfo
     * @return
     */
    @PostMapping("/invokeMethod")
    public DataResponse invokeMethod(@RequestBody MethodInfoDTO methodInfo, HttpServletResponse response,
                                     @CookieValue("historyClass")LinkedHashMap<String,ClassDTO> historyClass,
                                     @CookieValue("historyMethodInfo") Queue<MethodInfoDTO> historyMethod){
        if(CollectionUtils.isEmpty(historyMethod)){
            historyMethod=new LinkedList<>();
        }
        if(CollectionUtils.isEmpty(historyClass)){
            historyClass=new LinkedHashMap<>();
        }
        historyMethod.add(methodInfo);

        ClassDTO classDTO=new ClassDTO();
        classDTO.setValue(methodInfo.getClassName());
        historyClass.put(methodInfo.getClassName(),classDTO);

        //记录本次调用的信息   [使用类]  [使用方法]  [使用参数]
        Cookie hMethod=new Cookie("historyMethodInfo",historyMethod.toString());
        //记录本次调用类名
        Cookie hClass=new Cookie("historyClass",historyClass.toString());
        response.addCookie(hClass);
        response.addCookie(hMethod);

        //获得具体方法
        DataResponse<Method> method =
                locationMethodService.getMethod(methodInfo);
        Method data = method.getData();

        //调用方法
        methodService.invokeMethod(data, methodInfo.getParamValue());
        return DataResponse.buildSuccess();
    }


}
