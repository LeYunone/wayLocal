package com.leyuna.waylocation.dao;

import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @create 2021-08-10 14:49
 *  user表原子对象
 */
@Service
public class UserRepository extends BaseRepository<UserMapper, UserDO, UserCO> implements UserGateway {

}
