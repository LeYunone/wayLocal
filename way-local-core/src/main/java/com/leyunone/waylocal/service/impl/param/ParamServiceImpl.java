package com.leyunone.waylocal.service.impl.param;

import com.leyunone.waylocal.bean.response.DataResponse;
import com.leyunone.waylocal.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2022-02-24 14:59
 * 参数服务
 */
@Service
public class ParamServiceImpl implements ParamService {

    @Autowired
    private StructureAnalysisServiceImpl structureAnalysisServiceImpl;

    /**
     * 获取入参结构
     *
     * @return
     */
    public List<String> getParam(Class<?>[] cs) {
        List<String> result = new ArrayList<>();
        //如果是多参数的情况
        if (cs.length >= 1) {
            for (Class clazz : cs) {
                String json = structureAnalysisServiceImpl.getObjectStructure(clazz);
                result.add(json);
            }
        }
        //返回出去的是 以逗号分隔的json格式的参数结构
        return result;
    }

    /**
     * 获取出参结构
     *
     * @param clazz
     * @return
     */
    public String getReturnParam(Class<?> clazz) {
        String json = structureAnalysisServiceImpl.getObjectStructure(clazz);
        return json;
    }
}
