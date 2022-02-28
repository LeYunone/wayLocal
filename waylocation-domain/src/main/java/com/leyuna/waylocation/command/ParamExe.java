package com.leyuna.waylocation.command;

import com.leyuna.waylocation.constant.global.ServerConstant;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

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
    public void resoleParam(Object obj,Class type){
        try {
            //先判断这个对象是不是项目内对象需要级层分析
            if(ServerConstant.ClassName.contains(type.getName())){
                //得到所有属性值
                Field[] declaredFields = type.getDeclaredFields();
                for(Field field:declaredFields){
                    Class<?> aClass = field.getType();
                    boolean accessible = field.isAccessible();
                    try {
                        if(!accessible){
                            field.setAccessible(true);
                        }
                        //如果这个属性值是项目内属性，则进行迭代继续迭代
                        if(ServerConstant.ClassName.contains(aClass.getName())){
                            Object o = aClass.newInstance();
                            //迭代对象内属性
                            resoleParam(o,aClass);
                            field.set(obj,o);
                        }else{
                            //给属性赋值
                            if(aClass.isPrimitive()){
                                //如果是基本数据类型
                                continue;
                            }
                            //如果是集合类型则创建一个初始化对象进去
                            field.set(obj,null);
                        }
                    }catch (Exception e){
                    }finally {
                        field.setAccessible(accessible);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
