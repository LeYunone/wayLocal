package com.leyuna.waylocation.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.bean.dto.ClassDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.InvokeMethodService;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
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
                                     HttpServletRequest request,
                                     @CookieValue(value = "historyClass",required = false)String historyClass,
                                     @CookieValue(value = "historyMethodInfo",required = false) String historyMethod) throws UnsupportedEncodingException {
        LinkedHashMap<String,ClassDTO> hisC=new LinkedHashMap<>();
        Queue<MethodInfoDTO> hisM=new LinkedList<>();
        Cookie[] cookies = request.getCookies();
        if(!StringUtils.isEmpty(historyMethod)){
            hisM= new LinkedList<>(JSONObject.parseArray(historyMethod, MethodInfoDTO.class));
        }
        if(!StringUtils.isEmpty(historyClass)){
            hisC = JSONObject.parseObject(historyClass, hisC.getClass());
        }

        //调用方法
        DataResponse dataResponse = methodService.invokeMethod(methodInfo);
        Object data = dataResponse.getData();
        if(data!=null){
            //记录本次调用结果
            methodInfo.setReturnParamValue(JSON.toJSONString(data));
        }

        hisM.add(methodInfo);
        //记录本次调用的信息   [使用类]  [使用方法]  [使用参数]
        Cookie hMethod=new Cookie("historyMethodInfo",URLEncoder.encode(JSON.toJSONString(hisM),"UTF-8") );
        response.addCookie(hMethod);

        //记录本次调用的类
        ClassDTO classDTO=new ClassDTO();
        classDTO.setValue(methodInfo.getClassName());
        hisC.put(methodInfo.getClassName(),classDTO);
        Cookie hClass=new Cookie("historyClass", URLEncoder.encode(JSON.toJSONString(hisC),"UTF-8"));
        hClass.setPath("/");
        response.addCookie(hClass);

        //返回调用结果
        return DataResponse.of(methodInfo);
    }

    private void editHisCookie(String historyClass,String historyMethod,HttpServletResponse res){

    }
}
