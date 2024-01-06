package com.leyunone.waylocal.command;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.leyunone.waylocal.constant.global.ServerConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author leyunone
 * @create 2022-02-25 14:32
 * 参数指令
 */
@Service
public class ParamExe {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取对象的完整结构
     *
     * @return
     */
    public String getObjectStructure (Class clazz) {
        if (clazz.isPrimitive()) {
            //如果是基本数据类型
            return clazz.getName();
        }
        //入参对象
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            //入参无法实例化，则说明参数为抽象属性 或者没有空构造
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                //如果是集合类 实例一个大概的出去
                if (Collection.class.isAssignableFrom(clazz)) {
                    Type genericSuperclass = clazz.getGenericSuperclass();
                    Collection collection = null;
                    try {
                        collection = collectionLogic(clazz, genericSuperclass);
                    } catch (Exception e2) {
                        //理论上没有错误出现
                    }
                    return JSONObject.toJSONString(collection, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
                }
                //其余场景返回参数类型
                return clazz.getName();
            }
            //如果是包装类，走包装类逻辑里
            Object pagkLogic = packClassLogic(clazz);
            if(!StringUtils.isEmpty(pagkLogic)){
                return String.valueOf(pagkLogic);
            }

            //TODO 如果是没有无参构造场景时
            logger.error("waylocal : Error getObjectStructure error");
        }
        //深度解析对象结构  规则：如果是项目内对象则继续，否则跳过
        resoleParam(obj, clazz);
        return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 解析参数结构
     */
    private void resoleParam (Object obj, Class type) {
        if (null == obj) {
            return;
        }
        //先判断这个对象是不是项目内对象需要级层分析
        if (ServerConstant.ClassName.contains(type.getName())) {
            //得到所有属性值
            Field[] declaredFields = type.getDeclaredFields();
            for (Field field : declaredFields) {
                Class aClass = field.getType();
                if (aClass.isPrimitive()) {
                    //如果是基本数据类型
                    continue;
                }
                boolean accessible = field.isAccessible();
                try {
                    if (!accessible) {
                        field.setAccessible(true);
                    }
                    //如果这个属性值是项目内属性，则进行迭代继续迭代
                    if (ServerConstant.ClassName.contains(aClass.getName())) {
                        Object o = null;
                        try {
                            o = aClass.newInstance();
                        } catch (Exception e) {
                            //如果是抽象属性则跳过
                            if (aClass.isInterface() || Modifier.isAbstract(aClass.getModifiers())) {
                                continue;
                            }
                            //TODO 无参构造处理
                        }
                        //迭代对象内属性
                        resoleParam(o, aClass);
                        field.set(obj, o);
                    } else {
                        //获取属性泛型
                        Type genericType = field.getGenericType();
                        //判断是否是集合一类
                        if (Collection.class.isAssignableFrom(aClass)) {

                            Collection collection = collectionLogic(aClass, genericType);
                            //没有指定泛型的集合
                            field.set(obj, collection);
                            continue;
                        }
                        if (Map.class.isAssignableFrom(aClass)) {
                            //Map 逻辑

                        }
                        if (String.class.isAssignableFrom(aClass)) {
                            //String 逻辑
                            field.set(obj, " ");
                            continue;
                        }
                        //准备包装类逻辑  只有两种情况  不是包装类返回null   是包装类，返回默认值
                        Object packLogic = packClassLogic(aClass);
                        field.set(obj, packLogic);
                    }
                } catch (Exception e) {
                    logger.error("waylocal : Error resoleParam error");
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }
    }

    /**
     * 当class为collection 子接口或抽象类时，分类讨论 取出适合class的子类实例
     *
     * @param clazz
     * @return
     */
    private Collection getNewInstanceWhenCollection (Class clazz) {
        //判断是否是集合
        if (!Collection.class.isAssignableFrom(clazz)) {
            return null;
        }

        //判断是list分支吗
        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList();
        }

        //判断是set分支吗
        if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet();
        }

        //判断是队列分支吗
        if (Queue.class.isAssignableFrom(clazz)) {
            return new LinkedList();
        }
        return null;
    }

    private Map getNewInstanceWhenMap (Class clazz) {
        if (!Map.class.isAssignableFrom(clazz)) {
            return null;
        }

        if (Map.class.isAssignableFrom(clazz)) {
            return new HashMap();
        }

        return null;
    }

    /**
     * 包装类逻辑
     */
    private Object packClassLogic (Class clazz) {
        if (Byte.class.isAssignableFrom(clazz) || Short.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)) {
            return 0;
        }
        if (Double.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz)) {
            return 0.0;
        }
        if (Character.class.isAssignableFrom(clazz)) {
            return ' ';
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return true;
        }
        return null;
    }

    /**
     * 集合类逻辑
     *
     * @param aClass
     * @param genericType
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Collection collectionLogic (Class aClass, Type genericType) throws IllegalAccessException, InstantiationException {
        //Collection 逻辑
        Collection collection = null;
        //如果是接口或抽象类
        if (aClass.isInterface() || Modifier.isAbstract(aClass.getModifiers())) {
            //实例化它的可用类
            collection = getNewInstanceWhenCollection(aClass);
        } else {
            //如果是可实例子类，则直接调用
            collection = (Collection) aClass.newInstance();
        }

        if (genericType instanceof ParameterizedType) {
            //泛型实例
            ParameterizedType pt = (ParameterizedType) genericType;
            Class<?> tempC = (Class<?>) pt.getActualTypeArguments()[0];

            //属性
            Object instance = null;
            try {
                instance = tempC.newInstance();
            } catch (Exception e) {
                //TODO  如果泛型是抽象属性 或是没有无参构造场景暂时跳过
            }
            //如果是项目内的集合泛型 则创建一个初始化状态的对象进去
            if (ServerConstant.ClassName.contains(tempC.getName())) {
                //迭代解析
                resoleParam(instance, tempC);
            }
            //添加一个初始化对象进去
            collection.add(instance);
        }
        return collection;
    }
}
