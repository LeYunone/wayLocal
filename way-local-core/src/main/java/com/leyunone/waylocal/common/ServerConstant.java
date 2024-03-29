package com.leyunone.waylocal.common;

import com.leyunone.waylocal.bean.dto.MethodExcelDTO;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author leyunone
 * @create 2022-02-25 11:00
 * 应用通用常数
 */
public class ServerConstant {

    //项目内所有类的类名
    public static Set<String> ClassName=new HashSet<>();
    
    public static String saveType = null;
    
    public static String savePath = null;

    public static String saveMethod = "waylocal-history";
    
    public static Stack<MethodInfoVO> historyMethod = null;

    public static List<MethodExcelDTO> historyExcel = null;
}
