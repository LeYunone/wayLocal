package com.leyuna.waylocation.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pengli
 * @create 2022-04-13 14:04
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MethodExcelDTO {

    @ExcelProperty("类名")
    private String className;

    @ExcelProperty("方法名")
    private String methodName;

    @ExcelProperty("入参值")
    private List<String> paramValue;

    @ExcelProperty("出参值")
    private String returnParamValue;

    @ExcelProperty("调用时间")
    private String invokeTime;
}
