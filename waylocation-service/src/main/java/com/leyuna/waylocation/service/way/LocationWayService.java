package com.leyuna.waylocation.service.way;

import com.leyuna.waylocation.bean.dto.LuceneDTO;
import com.leyuna.waylocation.command.LuceneExe;
import com.leyuna.waylocation.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @date  2022-02-21 13:59
 * 定位方法接口     [根据方法名+类名组合？ 展示项目内所有条件方法？]
 */
@Service
public class LocationWayService {

    @Autowired
    private LuceneExe luceneExe;

    /**
     * 获得方法 最近使用的方法
     * @return DataResponse
     */
    public DataResponse getWay(){
        return DataResponse.buildSuccess();
    }

    /**
     * 根据类名[全类名] + 方法名 直接定位方法； 类名支持模糊
     * @param className
     * @param methodName
     * @return
     */
    public DataResponse getWay(String className,String methodName){
        return DataResponse.buildSuccess();
    }

    /**
     * 只根据方法名模糊
     * @param methodName
     * @return
     */
    public DataResponse getWay(String methodName,int size){

        //默认走索引库搜索拿出最近十条匹配的数据展示
        LuceneDTO methodDirByMethodName = luceneExe.getMethodDirByMethodName(methodName, size);
        return DataResponse.of(methodDirByMethodName);
    }
}
