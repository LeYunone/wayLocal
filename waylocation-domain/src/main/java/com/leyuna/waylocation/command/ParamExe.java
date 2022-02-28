package com.leyuna.waylocation.command;

import com.leyuna.waylocation.constant.global.ServerConstant;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author pengli
 * @create 2022-02-25 14:32
 * 参数指令
 */
@Service
public class ParamExe {

    /**
     * 解析参数结构
     */
    public void resoleParam (Object obj, Class type) {
        if (null == obj) {
            return;
        }
        //先判断这个对象是不是项目内对象需要级层分析
        if (ServerConstant.ClassName.contains(type.getName())) {
            //得到所有属性值
            Field[] declaredFields = type.getDeclaredFields();
            for (Field field : declaredFields) {
                Class aClass = field.getType();
                boolean accessible = field.isAccessible();
                try {
                    if (!accessible) {
                        field.setAccessible(true);
                    }
                    //如果这个属性值是项目内属性，则进行迭代继续迭代
                    if (ServerConstant.ClassName.contains(aClass.getName())) {
                        Object o = aClass.newInstance();
                        //迭代对象内属性
                        resoleParam(o, aClass);
                        field.set(obj, o);
                    } else {
                        //给属性赋值
                        if (aClass.isPrimitive()) {
                            //如果是基本数据类型
                            continue;
                        }
                        //判断是否是集合一类
                        Type genericType = field.getGenericType();
                        if (Collection.class.isAssignableFrom(aClass)) {
                            //List 逻辑
                            if(genericType  instanceof ParameterizedType){
                                ParameterizedType pt=(ParameterizedType)genericType;
                                Class<?> tempC= (Class<?>) pt.getActualTypeArguments()[0];
                                //如果是项目内的集合泛型 则创建一个初始化状态的对象进去
                                if(ServerConstant.ClassName.contains(tempC.getName())){
                                    Object o = tempC.newInstance();
                                    //TODO  创建原对象接口的实例类
                                    MyHandler myHandler = new MyHandler();//类
                                    Proxy.newProxyInstance(aClass.getClassLoader(),new Class[]{aClass},myHandler);
                                    List<Object> list=new ArrayList<>();
                                    //迭代解析
                                    resoleParam(o,o.getClass());
                                    list.add(o);
                                    field.set(obj,list);
                                    continue;
                                }
                            }
                        }
                        if (Map.class.isAssignableFrom(aClass)) {
                            //Map 逻辑

                        }

                        field.set(obj, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }
    }
}
