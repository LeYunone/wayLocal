package com.leyuna.waylocation.command;

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
        UserDO userDO = userMapper.selectById(1);
        return userDO;
    }
}
