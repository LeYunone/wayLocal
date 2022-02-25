package com.leyuna.waylocation.service.param;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.leyuna.waylocation.command.ParamExe;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.response.DataResponse;
import com.sun.deploy.util.StringUtils;
import org.jeasy.random.EasyRandom;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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
    public DataResponse getParam(Method method) throws Exception {
        Parameter[] parameters = method.getParameters();
        List<String> result=new ArrayList<>();
        //如果是多参数的情况
        if(parameters.length>=1){
            for(Parameter parameter:parameters){
                Class<?> type = parameter.getType();
                //入参对象
                Object obj = type.newInstance();
                paramExe.resoleParam(obj,type);
                String json = JSONObject.toJSONString(obj,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
                result.add(json);
            }
        }
        //返回出去的是 以逗号分隔的json格式的参数结构
        return DataResponse.of(StringUtils.join(result,","));
    }

    /**
     *  获取出参结构
     * @param method
     * @return
     */
    public DataResponse getReturnParam(Method method){

        return DataResponse.buildSuccess();
    }
}
