package com.leyuna.waylocation.service.param;

import com.leyuna.waylocation.response.DataResponse;
import com.sun.xml.internal.bind.v2.TODO;
import org.jeasy.random.EasyRandom;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pengli
 * @create 2022-02-24 14:59
 * 参数服务
 */
@Service
public class ParamService {

    /**
     * 获取入参结构
     * @return
     */
    public DataResponse getParam(Method method){
        Parameter[] parameters = method.getParameters();
        Map<String,Object> map=new HashMap<>();
        Object o=null;
        //如果是多参数的情况
        if(parameters.length>=1){
            for(Parameter parameter:parameters){
                Class<?> type = parameter.getType();
//                System.out.println(type);
                //每个参数进行单独处理，添加到map中最后转化为json
                /**
                 *  TODO 测试阶段   EasyRandom X
                 *
                 */
//                o = new EasyRandom().nextObject(type);
            }
        }
        return DataResponse.of(o);
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
