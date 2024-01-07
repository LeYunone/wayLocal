package com.leyunone.waylocal.bean.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author leyunone
 * @create 2022-02-22 10:36
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MethodInfoDTO implements Serializable {

    private static final long serialVersionUID = 6133772627258154184L;

    /**
     * 全类名
     */
    private String className;

    /**
     * 类字节码
     */
    private Class<?> clazz;

    /**
     * 展示给页面看到字符串 [高亮版本]
     */
    private String hightLineKey;

    /**
     * 展示给页面看到字符串 [普通版本]
     */
    private String lineKey;

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

    /**
     * 方法涉及的db目录
     */
    private List<SqlInvokeDTO> sqlInvokeDTO;

    private String invokeTime;
}
