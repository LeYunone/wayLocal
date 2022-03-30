package com.leyuna.waylocation.dao;

import com.leyuna.waylocation.util.SpringContextUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * (User) 工作台
 *
 * @author pengli
 * @since 2021-12-22 16:52:57
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Setter
public class UserE implements Serializable {
    private static final long serialVersionUID = -24912766450991293L;

    private String id;

    private String userName;

    private String passWord;

    //===========自定义方法区==========
    private UserGateway gateway;

    public UserGateway getGateway () {
        if (Objects.isNull(this.gateway)) {
            this.gateway = SpringContextUtil.getBean(UserGateway.class);
        }
        return this.gateway;
    }

    public static UserE queryInstance () {
        return new UserE();
    }

    public static UserE of (Object data) {
        return TransformationUtil.copyToDTO(data, UserE.class);
    }

    public List<UserCO> selectByCon () {
        UserGateway gateway = this.getGateway();
        return gateway.selectByCon(this);
    }

    public UserCO save () {
        UserGateway gateway = this.getGateway();
        return gateway.insertOrUpdate(this);
    }

    /**
     * 根据id查询
     */
    public UserCO selectById () {
        return this.getGateway().selectById(this.getId());
    }

    /**
     * 更新
     */
    public boolean update () {
        UserGateway gateway = this.getGateway();
        return gateway.update(this);
    }

    public static boolean batchCreate (List<UserE> list) {
        return UserE.queryInstance().getGateway().batchCreate(list);
    }
}
