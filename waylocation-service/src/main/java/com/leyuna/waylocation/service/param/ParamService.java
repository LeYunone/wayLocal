package com.leyuna.waylocation.service.param;

import com.leunya.waylocation.command.ParamExe;
import com.leunya.waylocation.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2022-02-24 14:59
 * 参数服务
 */
@Service
public class ParamService {

    @Autowired
    private ParamExe paramExe;

    /**
     * 获取入参结构
     * @return
     */
    public DataResponse getParam(Class<?> [] cs){
        List<String> result=new ArrayList<>();
        //如果是多参数的情况
        if(cs.length>=1){
            for(Class clazz:cs){
                String json = paramExe.getObjectStructure(clazz);
                result.add(json);
            }
        }else{
            return DataResponse.buildSuccess();
        }
        //返回出去的是 以逗号分隔的json格式的参数结构
        return DataResponse.of(result);
    }

    /**
     *  获取出参结构
     * @param clazz
     * @return
     */
    public DataResponse getReturnParam(Class<?> clazz){

        if(clazz==null){
            return DataResponse.buildSuccess();
        }

        String json = paramExe.getObjectStructure(clazz);
        return DataResponse.of(json);
    }
}
