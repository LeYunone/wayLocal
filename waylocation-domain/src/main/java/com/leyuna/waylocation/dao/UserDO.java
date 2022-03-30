package com.leyuna.waylocation.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (User)表实体类
 *
 * @author pengli
 * @since 2022-03-15 17:04:25
 */
@Getter
@Setter
@TableName("user")
public class UserDO implements Serializable {
    private static final long serialVersionUID = 617791215679106594L;
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String passWord;

    /**
     * 创建时间
     */
    @TableField(value = "create_Dt", fill = FieldFill.INSERT)
    private LocalDateTime createDt;

    /**
     * 更新时间
     */
    @TableField(value = "update_Dt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDt;

}
