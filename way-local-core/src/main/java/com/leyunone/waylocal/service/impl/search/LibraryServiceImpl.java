package com.leyunone.waylocal.service.impl.search;

import com.leyunone.waylocal.service.SearchLibraryService;
import com.leyunone.waylocal.support.lucene.SearchDbBuildService;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leyunone
 * @create 2022-02-21 17:19
 * 搜索库服务接口
 */
@Service
public class LibraryServiceImpl implements SearchLibraryService {

    @Autowired
    private SearchDbBuildService searchDbBuildService;

    /**
     * 解析整理出项目内所有的方法 [只有当项目第一次启动时使用]
     */
    public void resoleAllMethod(Class clazz) {
        //创建索引库前，检测是否已经有遗留记录
        this.deleteMethodDirFile();
        //拿到项目中所有方法信息
        List<MethodInfoDTO> methodInfoDTOS = null;


        //生成本项目方法的搜索库
        searchDbBuildService.addMethodDir(methodInfoDTOS);

        //生成本项目类的搜索库
        searchDbBuildService.addClassDir();

    }

    /**
     * 删除索引文件库
     */
    public void deleteMethodDirFile() {
        searchDbBuildService.deleteDir();
    }
}
