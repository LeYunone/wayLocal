package com.leyuna.waylocation.command;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyuna.waylocation.dao.UserCO;
import com.leyuna.waylocation.dao.UserDO;
import com.leyuna.waylocation.dao.UserE;
import com.leyuna.waylocation.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pengli
 * @create 2022-03-30 13:51
 */
@Service
public class SqlExe {

    @Autowired
    UserMapper userMapper;

    public UserDO sqlHandker(){
        System.out.println("执行");
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().lambda().eq(UserDO::getId,1)
                .eq(UserDO::getPassWord,"a3201360").eq(UserDO::getUserName,"leyuna"));
        userMapper.selectById(2);
        return userDO;
    }
}
