package com.leyunone.waylocal.constant.enums;

/**
 * @author leyunone
 * @create 2022-04-14 14:51
 */
public enum ResolveHistoryTypeEnum {

    SAVE("保存记录","0"),
    READ("读取记录","1");

    ResolveHistoryTypeEnum(String name,String value){
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
