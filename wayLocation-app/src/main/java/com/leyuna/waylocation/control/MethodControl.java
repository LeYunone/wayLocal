package com.leyuna.waylocation.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.bean.dto.ClassDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.constant.global.SqlInvokeConstant;
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
     *
     * @return
     */
    @RequestMapping("/getHistory")
    public DataResponse getHistory (HttpServletRequest request) {
        //需要先进后出的排列
        Stack<MethodInfoDTO> hisM = new Stack<>();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("historyClass")) {
                String value = cookie.getValue();
                MethodInfoDTO methodInfoDTO = JSONObject.parseObject(value, MethodInfoDTO.class);
                hisM.push(methodInfoDTO);
            }
        }
        return DataResponse.of(hisM);
    }


    /**
     * 方法调用
     *
     * @param methodInfo
     * @return
     */
    @PostMapping("/invokeMethod")
    public DataResponse invokeMethod (@RequestBody MethodInfoDTO methodInfo, HttpServletResponse response, HttpServletRequest request,
                                      @CookieValue(value = "historyClass", required = false) String historyClass) {
        if (SqlInvokeConstant.isGO) {
            return DataResponse.buildFailure("有一个数据库事务在进行");
        }
        //调用方法
        DataResponse dataResponse = methodService.invokeMethod(methodInfo);
        Object data = dataResponse.getData();
        if (data != null) {
            //记录本次调用结果
            methodInfo.setReturnParamValue(JSON.toJSONString(data));
        } else {
            methodInfo.setReturnParamValue("void");
        }
        //记录历史
//        editHisCookie(historyClass,request,methodInfo,response);
        methodInfo.setSqlInvokeDTO(SqlInvokeConstant.sqlInvokeDTO);
        //清空本次事务目录
        SqlInvokeConstant.sqlInvokeDTO = null;
        SqlInvokeConstant.isGO = false;
        return DataResponse.of(methodInfo);
    }

    @PostMapping("/export")
    public DataResponse export (@RequestBody List<MethodInfoDTO> methodInfoDTO, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(methodInfoDTO)) {
            return DataResponse.buildFailure("没有能够导出的数据");
        }

        return DataResponse.buildSuccess();
    }

    private void editHisCookie (String historyClass, HttpServletRequest request, MethodInfoDTO methodInfo, HttpServletResponse response) {
        //记录本次调用的信息   [使用类]  [使用方法]  [使用参数]
        try {
            Cookie cookieMethod = new Cookie("historyMethod:" + request.getCookies().length,
                    URLEncoder.encode(JSONObject.toJSONString(methodInfo), "UTF-8"));
            response.addCookie(cookieMethod);

            //记录本次调用的类
            LinkedHashMap<String, ClassDTO> hisC = new LinkedHashMap<>();
            if (!StringUtils.isEmpty(historyClass)) {
                hisC = JSONObject.parseObject(historyClass, hisC.getClass());
            }
            ClassDTO classDTO = new ClassDTO();
            classDTO.setValue(methodInfo.getClassName());
            hisC.put(methodInfo.getClassName(), classDTO);
            Cookie cookieClass = new Cookie("historyClass", URLEncoder.encode(JSON.toJSONString(hisC), "UTF-8"));
            cookieClass.setPath("/");
            response.addCookie(cookieClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
