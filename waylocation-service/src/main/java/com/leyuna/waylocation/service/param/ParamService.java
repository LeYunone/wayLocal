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
import java.util.List;

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
        List<String> result=new ArrayList<>();
        //如果是多参数的情况
        if(parameters.length>=1){
            for(Parameter parameter:parameters){
                Class<?> type = parameter.getType();
                //入参对象
                Object obj = null;
                try {
                    obj = type.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //深度解析对象结构  规则：如果是项目内对象则继续，否则跳过
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
