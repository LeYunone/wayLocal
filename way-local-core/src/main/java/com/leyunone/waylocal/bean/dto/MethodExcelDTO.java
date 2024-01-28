package com.leyunone.waylocal.bean.dto;

import lombok.*;


/**
 * @author leyunone
 * @create 2022-04-13 14:04
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MethodExcelDTO {

    private String className;

    private String methodName;

    private String paramValue;

    private String returnParamValue;

    private String invokeTime;
}
