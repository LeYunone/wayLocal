package com.leyuna.waylocation.command;

import com.leyuna.waylocation.constant.global.ServerConstant;
import org.springframework.stereotype.Service;

import java.lang.reflect.*;
import java.util.*;

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
                        //获取属性泛型
                        Type genericType = field.getGenericType();
                        //判断是否是集合一类
                        if (Collection.class.isAssignableFrom(aClass)) {
                            //Collection 逻辑
                            Collection collection = null;
                            //如果是接口或抽象类
                            if(aClass.isInterface()|| Modifier.isAbstract(aClass.getModifiers())){
                                //实例化它的可用类
                                collection = getNewInstanceWhenCollection(aClass);
                            }else{
                                //如果是可实例子类，则直接调用
                                collection = (Collection)aClass.newInstance();
                            }

                            if(genericType  instanceof ParameterizedType){
                                //泛型实例
                                ParameterizedType pt=(ParameterizedType)genericType;
                                Class<?> tempC= (Class<?>) pt.getActualTypeArguments()[0];

                                //如果是项目内的集合泛型 则创建一个初始化状态的对象进去
                                if(ServerConstant.ClassName.contains(tempC.getName())){
                                    //属性
                                    Object instance = tempC.newInstance();
                                    //迭代解析
                                    resoleParam(instance,instance.getClass());
                                    //添加一个初始化对象进去
                                    collection.add(instance);
                                }
                            }
                            //没有指定泛型的集合
                            field.set(obj,collection);
                            continue;
                        }
                        if (Map.class.isAssignableFrom(aClass)) {
                            //Map 逻辑

                        }
                        if(String.class.isAssignableFrom(aClass)){
                            //String 逻辑
                            field.set(obj," ");
                            continue;
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

    /**
     * 当class为collection 子接口或抽象类时，分类讨论 取出适合class的子类实例
     * @param clazz
     * @return
     */
    private Collection getNewInstanceWhenCollection(Class clazz){
        //判断是否是集合
        if(!Collection.class.isAssignableFrom(clazz)){
            return null;
        }

        //判断是list分支吗
        if(List.class.isAssignableFrom(clazz)){
            return new ArrayList();
        }

        //判断是set分支吗
        if(Set.class.isAssignableFrom(clazz)){
            return new HashSet();
        }

        //判断是队列分支吗
        if(Queue.class.isAssignableFrom(clazz)){
            return new LinkedList();
        }
        return null;
    }

    private Map getNewInstanceWhenMap(Class clazz){
        if(!Map.class.isAssignableFrom(clazz)){
            return null;
        }

        if(Map.class.isAssignableFrom(clazz)){
            return new HashMap();
        }

        return null;
    }
}
