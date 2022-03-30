package com.leyuna.waylocation.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 *基础Geteway
 * @author pengli
 * @since 2021-10-18
 */
public interface BaseGateway<CO> {

    /**
     *创建实体
     * @param entity
     * @return
     */
    CO insertOrUpdate(Object entity);

    /**
     *创建实体
     * @param entity
     * @return
     */
    boolean create(Object entity);

    /**
     * 批量创建
     * @param list
     * @return
     */
    boolean batchCreate(List list);

    /**
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int batchDelete(List<String> ids);

    /**
     * 根据ID更新一个实体
     * @param entity
     * @return
     */
    boolean update(Object entity);

    /**
     * 更新一个实体
     * @param entitys
     * @return
     */
    boolean batchUpdate(List entitys);

    /**
     * 根据ID查询出一个对象
     * @param id
     * @return
     */
    CO selectById(String id);

    /**
     * 根据ID列表批量查询
     * @param ids
     * @return
     */
    List<CO> selectByIds(List<String> ids);

    /**
     * 根据领域对象的设定的值来查询
     * @param con
     * @return
     */
    CO selectOne(Object con);

    /**
     * 根据领域对象的设定的值来查询
     * @param con
     * @return
     */
    List<CO> selectByCon(Object con);

    List<CO> selectByConOrder(Integer type,Object con);

    /**
     * 分页查询根据设定的值
     * @param con
     * @return
     */
    Page<CO> selectByPage (Object con,Integer index,Integer size);

    Page<CO> selectByConOrderPage(Object e,Integer index,Integer size,Integer type) ;

}
