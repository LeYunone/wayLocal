package com.leyuna.waylocation.start;

import com.leyuna.waylocation.config.SpringContextUtil;

import java.lang.reflect.Method;

/**
 * @author pengli
 * @create 2022-03-08 17:02
 */
public class test {

    public static void main (String[] args) throws Exception {
        Class<?> aClass = Class.forName("com.leyuna.waylocation.command.ClassExe");
        Method getTest = aClass.getDeclaredMethod("getTest");
        Object bean = null;
        try {
            bean = SpringContextUtil.getBean(aClass);
        }catch (Exception e){
            bean=aClass.newInstance();
        }
        getTest.invoke(bean);
        System.out.println("44");
    }
}
