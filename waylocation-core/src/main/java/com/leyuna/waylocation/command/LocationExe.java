package com.leyuna.waylocation.command;

import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.util.StringResoleUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2022-03-04 10:00
 * 定位指令
 */
@Service
public class LocationExe {

    /**
     * 定位方法
     * @param methodInfo
     * @return
     */
    public Method locationMethod(MethodInfoDTO methodInfo){
        try {
            Class<?> aClass = Class.forName(methodInfo.getClassName());
            Method method = null;
            String methodName = methodInfo.getMethodName();

            //过滤高亮字符
            methodName = StringResoleUtil.replaceString(methodName, "<span style='color:red'>");
            methodName = StringResoleUtil.replaceString(methodName,"</span>");
            try {
                method = aClass.getMethod(methodName, methodInfo.getParams());
            } catch (NoSuchMethodException e) {
                method = aClass.getDeclaredMethod(methodName,methodInfo.getParams());
            }
            return method;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
        }
        return null;
    }

    private List<String> getParams(Method method){
        List<String> result=new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for(Class clazz:parameterTypes){
            result.add(clazz.getName());
        }
        return result;
    }
}
