package com.leyunone.waylocal.constant.enums;

/**
 * @author leyunone
 * @create 2022-02-22 14:51
 * 路径
 */
public enum  PathEnum {

    PATH_METHOD_DIR("C:/dir/methodDir","方法索引库路径"),
    
    PATH_CLASS_DIR("C:/dir/classDir","类搜索库路径");

    PathEnum(String path,String remark){
        this.path=path;
        this.remark=remark;
    }

    private String path;

    private String remark;

    public String getName(){
        return this.remark;
    }

    public String getValue(){
        return this.path;
    }
}
