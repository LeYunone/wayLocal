package com.leyuna.waylocation.service.search;

import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.command.ClassExe;
import com.leyuna.waylocation.command.LuceneExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pengli
 * @create 2022-02-21 17:19
 *  搜索库服务接口
 */
@Service
public class SearchService {
    
    @Autowired
    private ClassExe classExe;

    @Autowired
    private LuceneExe luceneExe;

    /**
     * 解析整理出项目内所有的方法 [只有当项目第一次启动时使用]
     * 
     */
    public void resoleAllMethod(Class clazz){
        //拿到项目中所有方法信息
        List<MethodInfoDTO> methodInfoDTOS = classExe.orderClassToMethod(clazz.getClassLoader());

        //生成本项目方法的搜索库
        luceneExe.addMethodDir(methodInfoDTOS);
    }
}
