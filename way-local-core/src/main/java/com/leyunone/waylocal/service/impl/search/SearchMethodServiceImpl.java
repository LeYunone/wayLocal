package com.leyunone.waylocal.service.impl.search;

import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.util.StringResoleUtil;

import java.lang.reflect.Method;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/21
 */
public class SearchMethodServiceImpl {

    /**
     * 定位方法
     * @param methodInfo
     * @return
     */
    public Method locationMethod(MethodInfoDTO methodInfo){
        try {
            Class<?> aClass = Class.forName(methodInfo.getClassName());
            Method method = null;
            String methodName = methodInfo.getMethodName();

            //过滤高亮字符
            methodName = StringResoleUtil.replaceString(methodName, "<span style='color:red'>");
            methodName = StringResoleUtil.replaceString(methodName,"</span>");
            try {
                method = aClass.getMethod(methodName, methodInfo.getParams());
            } catch (NoSuchMethodException e) {
                method = aClass.getDeclaredMethod(methodName,methodInfo.getParams());
            }
            return method;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
        }
        return null;
    }
}
