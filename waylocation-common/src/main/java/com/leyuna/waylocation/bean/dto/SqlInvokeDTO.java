package com.leyuna.waylocation.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> sql = new ArrayList<>();

    /**
     * 涉及表格
     */
    private List<String> sqlTable = new ArrayList<>();

    /**
     * sql条件
     */
    private List<String> sqlCondition = new ArrayList<>();

    /**
     * 涉及数据
     */
    private List<String> sqlData = new ArrayList<>();
}
