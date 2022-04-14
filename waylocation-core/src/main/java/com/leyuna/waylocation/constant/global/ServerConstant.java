package com.leyuna.waylocation.constant.global;

import com.leyuna.waylocation.dto.MethodExcelDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author pengli
 * @create 2022-02-25 11:00
 * 应用通用常数
 */
public class ServerConstant {

    //项目内所有类的类名
    public static Set<String> ClassName=new HashSet<>();
    
    public static String saveType = null;
    
    public static String savePath = null;

    public static String saveMethod = "waylocation-history";
    
    public static Stack<MethodInfoDTO> historyMethod = null;

    public static List<MethodExcelDTO> historyExcel = null;
}
