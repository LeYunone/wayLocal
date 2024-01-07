package com.leyunone.waylocal.constant.enums;

/**
 * @author leyunone
 * @create 2022-02-23 14:31
 */
public enum ErrorEnum {

    SELECT_INFO_NOT_FOUND("查询失败：没有找到与之匹对的信息","0");

    ErrorEnum(String name,String value){
        this.name=name;
        this.value=value;
    }

    private String name;

    private String value;

    public String getName(){
        return this.name;
    }

    public String getValue(){
        return this.value;
    }
}
