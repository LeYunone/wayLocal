package com.leyuna.waylocation.service.param;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.leyuna.waylocation.command.ParamExe;
import com.leyuna.waylocation.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public DataResponse getParam(Class<?> [] cs){
        Map<String,Object> result=new HashMap<>();
        //如果是多参数的情况
        if(cs.length>=1){
            for(Class clazz:cs){
                if(clazz.isPrimitive()){
                    continue;
                }
                String json = paramExe.getObjectStructure(clazz, clazz.getName());
                result.put(clazz.getName(),json);
            }
        }else{
            return DataResponse.buildSuccess();
        }
        //返回出去的是 以逗号分隔的json格式的参数结构
        return DataResponse.of(result);
    }

    /**
     *  获取出参结构
     * @param clazz
     * @return
     */
    public DataResponse getReturnParam(Class<?> clazz){
        String json = paramExe.getObjectStructure(clazz,null);
        return DataResponse.of(json);
    }
}
