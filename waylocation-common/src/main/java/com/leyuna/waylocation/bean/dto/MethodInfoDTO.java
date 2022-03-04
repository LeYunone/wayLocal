package com.leyuna.waylocation.bean.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author pengli
 * @create 2022-02-22 10:36
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MethodInfoDTO {

    /**
     * 全类名
     */
    private String className;

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
     * 参数列表 逗号分割
     */
    private String params;

    /**
     * 出参列表 逗号分割
     */
    private String returnParams;

    /**
     * 入参值 json格式
     */
    private String paramJsonValue;
}
