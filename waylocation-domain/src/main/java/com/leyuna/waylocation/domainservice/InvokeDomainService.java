package com.leyuna.waylocation.domainservice;

import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.command.LocationExe;
import com.leyuna.waylocation.util.AssertUtil;
import com.leyuna.waylocation.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author pengli
 * @create 2022-03-09 09:44
 */
@Component
public class InvokeDomainService {

    @Autowired
    private LocationExe locationExe;

    /**
     * 调用方法
     *
     * @param methodInfo
     * @return
     */
    public Object invokeMethod (MethodInfoDTO methodInfo) {
        //定位到具体方法
        Method method = locationExe.locationMethod(methodInfo);
        AssertUtil.isFalse(null == method, "系统无法定位到具体方法");
        boolean accessible = method.isAccessible();
        //实例到具体类
        Object bean = null;
        Class<?> aClass = methodInfo.getClazz();
        try {
            Class.forName(methodInfo.getClassName());
            bean = SpringContextUtil.getBean(aClass);
        } catch (Exception e) {
            try {
                bean = aClass.newInstance();
            } catch (Exception e2) {
                //如果这个方法是抽象属性 且没有注册到容器中，则返回提示
                AssertUtil.isFalse(true,"该方法没有注册到容器且无法实例");
            }
            e.printStackTrace();
        }
        //解析入参值
        Class<?>[] params = methodInfo.getParams();
        Object [] paramObjects=new Object[params.length];
        List<String> paramValue = methodInfo.getParamValue();
        for(int i=0;i<params.length;i++){
            String json=paramValue.get(i);
            Class clazz=params[i];
            Object o = JSONObject.parseObject(json, clazz);
            paramObjects[i]=o;
        }
        Object result=null;
        try {
            method.setAccessible(true);
            result=method.invoke(bean,paramObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            method.setAccessible(accessible);
        }
        return result;
    }
}
