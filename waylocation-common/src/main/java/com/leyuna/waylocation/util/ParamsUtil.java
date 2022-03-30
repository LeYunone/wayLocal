package com.leyuna.waylocation.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2022-02-24 16:53
 * 包装出入参工具方法
 */
public class ParamsUtil {

    /**
     * 返回类型类名 逗号分隔
     * @param cs 字节码数组
     * @return
     */
    public static String getParams(Class<?> [] cs){
        List<String> result=new ArrayList<>();
        for(Class clazz:cs){
            result.add(clazz.getName());
        }
        return StringUtils.join(result,",");
    }
}
