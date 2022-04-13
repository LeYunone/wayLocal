package com.leyuna.waylocation.service.method;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.command.InvokeMethodExe;
import com.leyuna.waylocation.config.EasyExcelOrderListener;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.MethodExcelDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author pengli
 * @create 2022-02-23 15:01
 * 调用方法服务
 */
@Service
public class InvokeMethodService {

    @Autowired
    private InvokeMethodExe invokeMethodExe;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DataResponse invokeMethod(MethodInfoDTO methodInfo,HttpServletResponse response, HttpServletRequest request){
        //执行方法
        Object o = null;
        try {
            o = invokeMethodExe.invokeMethod(methodInfo);

            //历史保存任务
            switch (ServerConstant.saveType){
                case "cookie":
                    //cookie存储 少量信息
                    this.saveHistoryToCookie(methodInfo,request,response);
                    break;
                case "object":
                    //session存储 本次会话全部信息
                    this.saveHistoryToObject(o,methodInfo);
                    break;
                case "file":
                    //file存储 持久化全部信息
                    this.saveHistroyToFile(o,methodInfo);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            logger.error("Waylocation Error : "+e.getMessage());
        }
        return DataResponse.of(o);
    }

    /**
     * 调用记录存储到cookie中
     * @param
     * @param methodInfo
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     */
    private void saveHistoryToCookie(MethodInfoDTO methodInfo,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        //类名集合
        List<String> classList = new ArrayList<>();
        //记录集合
        List<MethodInfoDTO> methodList = new ArrayList<>();
        if(null != cookies){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(ServerConstant.saveClass)){
                    //取之前存储的类名集合
                    String value = cookie.getValue();
                    classList = JSONObject.parseArray(value,String.class);
                }
                if(cookie.getName().equals(ServerConstant.saveMethod)){
                    //去之前存储的记录集合
                    String value = cookie.getValue();
                    methodList = JSONObject.parseArray(value,MethodInfoDTO.class);
                }
            }
        }

        //存储调用类
        classList.add(methodInfo.getClassName());
        Set set = new HashSet();
        set.addAll(classList);
        List<String> resultList = new ArrayList<>(set);
        Cookie classCookie = new Cookie(ServerConstant.saveClass,JSONObject.toJSONString(resultList));

        //存储记录
        MethodInfoDTO methodInfoDTO = new MethodInfoDTO();
        methodInfoDTO.setClassName(methodInfo.getClassName());
        methodInfoDTO.setMethodName(methodInfo.getMethodName());
        methodList.add(methodInfoDTO);

        Cookie methodCookie = new Cookie(ServerConstant.saveMethod,
                JSONObject.toJSONString(methodList));

        //保存cookie
        response.addCookie(methodCookie);
        response.addCookie(classCookie);
    }


    /**
     * 保存全部信息到 应用中
     * @param o
     * @param methodInfo
     */
    private void saveHistoryToObject(Object o, MethodInfoDTO methodInfo){

        //存储本次调用记录
        methodInfo.setReturnParamValue(JSONObject.toJSONString(o));
        ServerConstant.historyMethod.push(methodInfo);

        //存储本次调用类
        ClassDTO classDTO = new ClassDTO();
        classDTO.setHightLineKey(methodInfo.getClassName());
        classDTO.setValue(methodInfo.getClassName());
        ServerConstant.historyClass.add(classDTO);
    }

    private void saveHistroyToFile (Object object, MethodInfoDTO methodInfo) {
        File file = new File(ServerConstant.savePath + "/history.xlsx");
        MethodExcelDTO excelDTO = new MethodExcelDTO();
        excelDTO.setClassName(methodInfo.getClassName());
        excelDTO.setInvokeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        excelDTO.setMethodName(methodInfo.getMethodName());
        excelDTO.setParamValue(methodInfo.getParamValue());
        excelDTO.setReturnParamValue(object!=null?JSONObject.toJSONString(object):"");
        ServerConstant.historyExcel.add(excelDTO);

        //写文档
        EasyExcel.write(file, MethodExcelDTO.class).sheet().doWrite(ServerConstant.historyExcel);

        logger.info("waylocation Success : file记录保存成功" );
    }


}
