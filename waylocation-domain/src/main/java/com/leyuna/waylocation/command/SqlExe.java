package com.leyuna.waylocation.command;

import com.leyuna.waylocation.dao.UserCO;
import com.leyuna.waylocation.dao.UserDO;
import com.leyuna.waylocation.dao.mapper.UserMapper;
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

    public UserCO sqlHandker(){
        System.out.println("执行");
        UserDO userDO=new UserDO();
        userDO.setId("4");
        List<UserCO> listAndTest = userMapper.getListAndTest(userDO);
        return listAndTest.get(0);
    }
}
