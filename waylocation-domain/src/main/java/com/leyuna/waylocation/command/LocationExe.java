package com.leyuna.waylocation.command;

import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.sun.deploy.util.StringUtils;
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
     *
     * @param className
     * 定位到指定类名下的所有方法信息
     * @return
     */
    public List<MethodInfoDTO> locationMethod(String className){

        List<MethodInfoDTO> result=new ArrayList<>();

        try {
            Class<?> aClass = Class.forName(className);
            Method[] declaredMethods = aClass.getDeclaredMethods();

            //遍历所有方法，判断其名
            for(Method m:declaredMethods){
                MethodInfoDTO methodInfoDTO=new MethodInfoDTO();
                methodInfoDTO.setClassName(className);
                methodInfoDTO.setMethodName(m.getName());
                methodInfoDTO.setValue(methodInfoDTO.getReturnParams()+"  "+m.getName()+"("+methodInfoDTO.getParams()+")");
                //入参列表
                methodInfoDTO.setParams(StringUtils.join(getParams(m),","));
                //出餐列表
                methodInfoDTO.setReturnParams(m.getReturnType().getName());
                result.add(methodInfoDTO);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
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
