package com.leyuna.waylocation.domainservice;

import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.command.LocationExe;
import com.leyuna.waylocation.util.AssertUtil;
import com.leyuna.waylocation.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        //返回参数
        Object result=null;

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
                return result="无法从容器中取得方法 或该方法无法实例";
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
            if(clazz.isPrimitive() && null==o){
                //如果是基本数据类型而且没有赋值，则给他基本默认值
                o = getPrimitive(clazz);
            }
            paramObjects[i]=o;
        }
        try {
            method.setAccessible(true);
            result=method.invoke(bean,paramObjects);
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        }finally {
            method.setAccessible(accessible);
        }
        return result;
    }
    
    private Object getPrimitive(Class clazz){
        if(clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class ){
            return 0;
        }
        if(clazz == double.class || clazz == float.class){
            return 0.0;
        }
        if(clazz == boolean.class){
            return  false;
        }
        if(clazz == char.class){
            return ' ';
        }
        return null; 
    }
}
