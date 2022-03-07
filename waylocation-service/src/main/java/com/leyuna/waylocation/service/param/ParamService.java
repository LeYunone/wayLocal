package com.leyuna.waylocation.service.param;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.leyuna.waylocation.command.ParamExe;
import com.leyuna.waylocation.response.DataResponse;
import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengli
 * @create 2022-02-24 14:59
 * 参数服务
 */
@Service
public class ParamService {

    @Autowired
    private ParamExe paramExe;

    /**
     * 获取入参结构
     * @return
     */
    public DataResponse getParam(Method method){
        Parameter[] parameters = method.getParameters();
        Map<String,Object> result=new HashMap<>();
        //如果是多参数的情况
        if(parameters.length>=1){
            for(Parameter parameter:parameters){
                Class<?> type = parameter.getType();
                String json = paramExe.getObjectStructure(type, parameter.getName());
                result.put(parameter.getName(),json);
            }
        }else{
            return DataResponse.buildSuccess();
        }
        //返回出去的是 以逗号分隔的json格式的参数结构
        return DataResponse.of(result);
    }

    /**
     *  获取出参结构
     * @param method
     * @return
     */
    public DataResponse getReturnParam(Method method){
        Class<?> returnType = method.getReturnType();
        String json = paramExe.getObjectStructure(returnType,null);
        return DataResponse.of(json);
    }
}
