package com.leyuna.waylocation.command;

import com.leyuna.waylocation.util.ClassOrderUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author pengli
 * @date 2022-02-21
 * 类处理 执行器
 */
@Component
public class ClassExe {

    /**
     * 遍历请求项目里所有的文件
     */
    public void orderClass(ClassLoader load){
        List<Class> clazzs = ClassOrderUtil.loadClassByLoader(load);
        //处理项目中所有的类，并且将其方法整理出来
        for(Class clazz:clazzs){
            Method[] declaredMethods = clazz.getDeclaredMethods();
            
            for(Method method:declaredMethods){
                
            }
        }
    }
}
