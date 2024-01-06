package com.leyunone.waylocal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pengli
 * @create 2022-03-31 15:34
 * 方法调用出参
 */
@Getter
@Setter
@ToString
public class SqlInvokeDTO {

    /**
     * 是否开启本次方法的sql监听  以下4个字段为同一纬度， sql[][][][]
     */
    private Integer goNum;

    /**
     * sql语句
     */
    private String sql;

    /**
     * 涉及表格
     */
    private String sqlTable ;

    /**
     * sql条件
     */
    private String sqlCondition;

    /**
     * 涉及数据
     */
    private String sqlData;

    /**
     * 本次sql操作
     */
    private String sqlAction;

    /**
     * 本次sql执行时间
     */
    private String sqlTime;
}
