package com.leyuna.waylocation.service.search;

import com.leyuna.waylocation.command.ClassExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @create 2022-02-21 17:19
 *  搜索库服务接口
 */
@Service
public class SearchService {
    
    @Autowired
    private ClassExe classExe;

    /**
     * 获得项目内所有类
     * 
     */
    public void getAllClassName(Class clazz){
        classExe.orderClass(clazz.getClassLoader());
    }
}
