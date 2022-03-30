package com.leyuna.waylocation.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象Repository服务类
 * @author pengli
 * @since 2021-10-18
 */
public abstract class BaseRepository<M extends BaseMapper<DO>, DO,CO> extends ServiceImpl<M, DO> implements BaseGateway<CO> {
    private Class COclass;
    private Class DOclass;
    public BaseRepository () {
        Class<?> c = getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) t).getActualTypeArguments();
            DOclass = (Class<?>)params[1];
            COclass = (Class<?>)params[2];
        }
    }

    /**
     * 创建实体
     *
     * @param entity
     * @return
     */
    @Override
    public CO insertOrUpdate(Object entity) {
        DO copy = (DO) TransformationUtil.copyToDTO(entity, DOclass);
        this.saveOrUpdate(copy);
        return  (CO)TransformationUtil.copyToDTO(copy,COclass);
    }

    /**
     * 创建实体
     *
     * @param entity
     * @return
     */
    @Override
    public boolean create(Object entity) {
        DO copy = (DO) TransformationUtil.copyToDTO(entity, DOclass);
        return this.save(copy);
    }

    /**
     * 批量创建
     *
     * @param list
     * @return
     */
    @Override
    public boolean batchCreate(List list) {
        List<DO> copy = TransformationUtil.copyToLists(list, DOclass);
        return this.saveBatch(copy);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public int delete(String id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public int batchDelete(List<String> ids) {
        return baseMapper.deleteBatchIds(ids);
    }

    /**
     * 更新一个实体
     *
     * @param entity
     * @return
     */
    @Override
    public boolean update(Object entity) {
        DO copy = (DO)TransformationUtil.copyToDTO(entity, DOclass);
        return baseMapper.updateById(copy)>0;
    }

    /**
     * 更新一个实体
     *
     * @param entitys
     * @return
     */
    @Override
    public boolean batchUpdate(List entitys) {
        List<DO> copy = TransformationUtil.copyToLists(entitys, DOclass);
        return this.updateBatchById(copy);
    }

    /**
     * 根据ID查询出一个对象
     *
     * @param id
     * @return
     */
    @Override
    public CO selectById(String id) {
        DO do_ = baseMapper.selectById(id);
        return (CO)TransformationUtil.copyToDTO(do_, COclass);
    }

    /**
     * 根据ID列表批量查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<CO> selectByIds(List<String> ids) {
        if(ids==null || ids.isEmpty()){
            return new ArrayList<>();
        }
        List<DO> DOS = baseMapper.selectBatchIds(ids);
        return TransformationUtil.copyToLists(DOS,COclass);
    }


    /**
     * 根据领域对象的设定的值来查询
     *
     * @param con
     * @return
     */
    @Override
    public CO selectOne(Object con){
        List<CO> list = this.selectByCon(con);
        if(!list.isEmpty()){
            return list.get(0);
        }else{
            return null;
        }
    }

    /**
     * 根据领域对象的设定的值来查询
     *
     * @param con
     * @return
     */
    @Override
    public List<CO> selectByCon(Object con){
        //delete设置为false
        deletedToFalse(con);
        Object copy = TransformationUtil.copyToDTO(con, DOclass);
        QueryWrapper<DO> dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false);
        List<DO> ds = this.baseMapper.selectList(dQueryWrapper);
        return TransformationUtil.copyToLists(ds, COclass);
    }

    @Override
    public List<CO> selectByConOrder(Integer type,Object con){
        //delete设置为false
        deletedToFalse(con);
        Object copy = TransformationUtil.copyToDTO(con, DOclass);
        QueryWrapper<DO> dQueryWrapper = null;
        switch (type){
            case 1:
                //创建时间
                dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false).orderByDesc("create_dt");
                break;
            case 2:
                dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false).orderByAsc("create_dt");
                break;
            case 3:
                dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false).orderByAsc("update_dt");
                break;
            case 4:
                dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false).orderByDesc("update_dt");
                break;
            default:
                dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false);
        }
        List<DO> ds = this.baseMapper.selectList(dQueryWrapper);
        return TransformationUtil.copyToLists(ds, COclass);
    }

    @Override
    public Page<CO> selectByPage (Object con,Integer index,Integer size) {
        Page<DO> page = new Page<>(index,size);
        Object copy = TransformationUtil.copyToDTO(con, DOclass);
        QueryWrapper<DO> dQueryWrapper = new QueryWrapper<DO>().allEq(TransformationUtil.transDTOColumnMap(copy), false);
        IPage<DO> doPage = this.baseMapper.selectPage(page, dQueryWrapper);

        return TransformationUtil.copyToPage(doPage,COclass);
    }

    /**
     * 分页查询 日期排序
     * @param e
     * @param type 排序类型   0为查询最近的日期排序   1为查最早的日期排序
     * @return
     */
    @Override
    public Page<CO> selectByConOrderPage(Object e,Integer index,Integer size,Integer type) {
        Map<String, Object> stringObjectMap = TransformationUtil.transDTOColumnMap(e);
        Page page=new Page(index,size);
        IPage<DO> ipage =null;
        switch (type){
            case 0:
                ipage=this.baseMapper.selectPage(page, new QueryWrapper<DO>().allEq(stringObjectMap).orderByDesc("create_dt"));
                break;
            case 1:
                ipage=this.baseMapper.selectPage(page, new QueryWrapper<DO>().allEq(stringObjectMap).orderByAsc("create_dt"));
                break;
            case 2:
                ipage=this.baseMapper.selectPage(page, new QueryWrapper<DO>().allEq(stringObjectMap).orderByDesc("update_dt"));
                break;
            default:
                break;
        }

        return TransformationUtil.copyToPage(ipage,COclass);
    }

    /**
     * 如果包含字段deleted，且值为空，那么给上默认值0
     * @param con
     */
    private void deletedToFalse(Object con){
        Class<?> aClass = con.getClass();
        try {
            Field deletedField = aClass.getDeclaredField("deleted");
            boolean accessible = deletedField.isAccessible();
            try{
                if(!accessible){
                    deletedField.setAccessible(true);
                }
                Object value = deletedField.get(con);
                if(value==null){
                    deletedField.set(con,0);
                }
            }finally {
                if(!accessible){
                    deletedField.setAccessible(accessible);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }
}
