package com.leyuna.waylocation.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyuna.waylocation.dao.UserCO;
import com.leyuna.waylocation.dao.UserDO;

import java.util.List;

/**
 * @author pengli
 * @create 2021-08-10 16:27
 */
public interface UserMapper extends BaseMapper<UserDO> {

    List<UserCO> getListAndTest(UserDO userDO);
}
