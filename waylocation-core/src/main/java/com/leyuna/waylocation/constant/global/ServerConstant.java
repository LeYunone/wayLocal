package com.leyuna.waylocation.constant.global;

import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;

import java.util.HashSet;
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

    public static String saveClass = "waylocation-class";
    
    public static String saveMethod = "waylocation-history";
    
    public static Set<ClassDTO> historyClass = null;
    
    public static Stack<MethodInfoDTO> historyMethod = null;
}
