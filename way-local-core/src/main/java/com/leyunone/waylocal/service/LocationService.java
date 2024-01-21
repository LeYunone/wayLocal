package com.leyunone.waylocal.service;

import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.vo.ClassInfoVO;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;

import java.lang.reflect.Method;
import java.util.List;

/**
 * :)
 *
 * @Author leyunone
 * @Date 2024/1/9 11:08
 */
public interface LocationService {

    List<ClassInfoVO> getClassName(String className, Integer size);

    Method getMethod(MethodInfoDTO methodInfo);

    List<MethodInfoVO> getMethod(String className, String method);

    List<MethodInfoVO> getMethod(String methodName, Integer size);

    MethodInfoVO getOptimalMatch(String className, String methodName);
}
