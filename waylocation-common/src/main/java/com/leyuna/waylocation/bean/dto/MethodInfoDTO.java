package com.leyuna.waylocation.bean.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author pengli
 * @create 2022-02-22 10:36
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MethodInfoDTO implements Serializable {

    /**
     * 全类名
     */
    private String className;

    /**
     * 类字节码
     */
    private Class<?> clazz;

    /**
     * 展示给页面看到字符串
     */
    private String value;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法标识
     */
    private String methodId;

    /**
     * 参数列表 
     */
    private Class<?>[] params;

    /**
     * 入参值 json格式
     */
    private List<String> paramValue;

    /**
     * 出参列表 逗号分割
     */
    private Class<?> returnParams;

    /**
     * 出参值
     */
    private String returnParamValue;
}
