package com.leyuna.waylocation.command;

import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.util.ClassOrderUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @date 2022-02-21
 * 类处理 执行器
 */
@Service
public class ClassExe {

    /**
     * 遍历请求项目里所有的文件
     */
    public List<MethodInfoDTO> orderClassToMethod(ClassLoader load){
        List<MethodInfoDTO> result=new ArrayList<>();
        List<Class> clazzs = ClassOrderUtil.loadClassByLoader(load);
        int numId=0;
        //处理项目中所有的类，并且将其方法整理出来
        for(Class clazz:clazzs){
            Method[] declaredMethods = clazz.getDeclaredMethods();
            clazz.getMethods();
            ServerConstant.ClassName.add(clazz.getName());
            //封装项目内所有方法的信息
            for(int i=0;i<declaredMethods.length;i++,numId++){
                Method method=declaredMethods[i];
                MethodInfoDTO methodInfoDTO=new MethodInfoDTO();
                methodInfoDTO.setClassName(clazz.getName());
                //记录类名到应用中
                methodInfoDTO.setMethodId(String.valueOf(numId));
                //类字节码
                methodInfoDTO.setClazz(clazz);
                methodInfoDTO.setMethodName(method.getName());
                //入参列表
                methodInfoDTO.setParams(method.getParameterTypes());
                //出餐列表
                methodInfoDTO.setReturnParams(method.getReturnType());
                result.add(methodInfoDTO);
            }
        }
        return result;
    }

    private ClassDTO getEEEaaa(List<String> list, ClassDTO testDto){
        System.out.println(1);
        ClassDTO classDTO = new ClassDTO();
        classDTO.setHightLineKey("test");
        classDTO.setValue("测试");
        return  classDTO;
    }

    public void getTest(){
        System.out.println("22");
        System.out.println("33");
    }
}