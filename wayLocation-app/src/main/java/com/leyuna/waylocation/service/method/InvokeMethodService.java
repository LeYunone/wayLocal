package com.leyuna.waylocation.service.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.command.HistoryExe;
import com.leyuna.waylocation.command.InvokeMethodExe;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.constant.global.SqlInvokeConstant;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    @Autowired
    private HistoryExe historyExe;

    public DataResponse invokeMethod(MethodInfoDTO methodInfo,HttpServletResponse response, HttpServletRequest request){
        //执行方法
        Object o = null;
        try {
            o = invokeMethodExe.invokeMethod(methodInfo);

            //历史保存任务
            switch (ServerConstant.saveType){
                case "cookie":
                    //cookie存储 少量信息
                    this.saveHistoryCookie(o,methodInfo,response);
                    break;
                case "session":
                    //session存储 本次会话全部信息
                    this.saveHistorySession(o,methodInfo,request);
                    break;
                case "file":
                    //file存储 持久化全部信息
                    historyExe.saveHistroyToFile(o,methodInfo);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            logger.error("Waylocation Error : "+e.getMessage());
        }
        return DataResponse.of(o);
    }


    private void saveHistoryCookie(Object o, MethodInfoDTO methodInfo, HttpServletResponse response) throws UnsupportedEncodingException {
        MethodInfoDTO methodInfoDTO = new MethodInfoDTO();
        methodInfoDTO.setClassName(methodInfo.getClassName());
        methodInfoDTO.setMethodName(methodInfo.getMethodName());

        Cookie cookie = new Cookie("Waylocation:"+methodInfo.getMethodId(),
                URLEncoder.encode(JSONObject.toJSONString(methodInfo), "UTF-8"));
        response.addCookie(cookie);
    }

    private void saveHistorySession(Object o, MethodInfoDTO methodInfo,HttpServletRequest request){
        HttpSession session = request.getSession();

        //保存本次调用类名class
        Object classNames = session.getAttribute("waylocation:class");
        List<String> classList = new ArrayList<>();
        if(null != classNames){
            classList = JSONObject.parseArray(classNames.toString(),String.class);
        }
        classList.add(methodInfo.getClassName());
        //去重
        Set set = new HashSet();
        set.addAll(classList);
        List<String> classResult = new ArrayList(set);
        session.setAttribute("waylocation:class", JSONObject.toJSON(classResult));

        //保存本次方法
        Object historys = session.getAttribute("waylocation:history");
        List<MethodInfoDTO> methodInfoDTOS = new ArrayList<>();
        if(null != historys){
            methodInfoDTOS = JSONObject.parseArray(historys.toString(), MethodInfoDTO.class);
        }
        methodInfo.setReturnParamValue(JSONObject.toJSONString(o));
        methodInfoDTOS.add(methodInfo);
        session.setAttribute("waylocation:history", JSONObject.toJSON(methodInfoDTOS));
    }
}
