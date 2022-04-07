package com.leunya.waylocation.util;

/**
 * @author pengli
 * @create 2022-02-22 17:18
 */
public class StringResoleUtil {

    /**
     * 拆解单词  空格分隔 大写区分的单词
     * @param name
     * @return
     */
    public static String disassembleWord(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 循环处理字符
            for (int i = 0; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加空格
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append(" ");
                }
                result.append(s);
            }
        }
        return result.toString();
    }

    /**
     * 合成单词 去掉空格
     * @param word
     * @return
     */
    public static String synthesisWord(String word){
        return word.replaceAll(" ","");
    }

    public static String replaceString(String str,String replace){
        return str.replaceAll(replace,"");
    }
}
