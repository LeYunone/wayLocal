package com.leyuna.waylocation.util;

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
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append(" ");
                }
                result.append(s);
            }
        }
        return result.toString();
    }
}
