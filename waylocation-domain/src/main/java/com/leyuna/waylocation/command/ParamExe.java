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
                    //如果这个属性值是项目内属性，则进行迭代继续迭代
                    if(ServerConstant.ClassName.contains(field.getClass().getName())){

                    }else{
                        //给属性赋值
                        field.set(obj,null);
                    }
                }
            }
        }catch (Exception e){
        }
    }
}
