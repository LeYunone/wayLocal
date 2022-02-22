package com.leyuna.waylocation.constant.enums;

/**
 * @author pengli
 * @create 2022-02-22 14:51
 * 路径
 */
public enum  PathEnum {

    PATH_METHOD_DIR(System.getProperty("user.dir")+"/dir/methodDir","方法索引库路径");

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
