//package com.leyuna.waylocation.util;
//
///**
// * @author pengli
// * @create 2021-08-13 09:31
// *
// * 报错处理
// */
//public class AssertUtil {
//
//    public static void isFalse(boolean condition,String message){
//        isFalse(condition,new RuntimeException(message));
//    }
//
//    public static void isFalse(boolean condition,RuntimeException ex){
//        isTrue(!condition,ex);
//    }
//
//    public static void isTrue(boolean condition,RuntimeException ex){
//        if(!condition){
//            throw ex;
//        }
//    }
//
//    public static void isTrue(boolean condition,String msg){
//        if(!condition){
//            throw new RuntimeException(msg);
//        }
//    }
//
//    public static void isTrue(String msg){
//        throw new RuntimeException(msg);
//    }
//}
