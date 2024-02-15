package com.leyunone.waylocal.common;


import com.leyunone.waylocal.bean.dto.SqlInvokeDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leyunone
 * @create 2022-03-31 15:32
 * 方法调用sql生成的常数 ： 涉及表 、sql语句、涉及数据等
 */
public class SqlInvokeConstant {
    
    public static Map<String,List<SqlInvokeDTO>> SQL_INFO = new ConcurrentHashMap<>();

}
