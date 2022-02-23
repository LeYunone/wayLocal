package com.leyuna.waylocation.service.way;

import com.leyuna.waylocation.bean.dto.LuceneDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.command.LuceneExe;
import com.leyuna.waylocation.constant.enums.ErrorEnum;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.util.AssertUtil;
import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.management.MethodInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        List<MethodInfoDTO> result=new ArrayList<>();

        //先根据类名从搜索库查到最接近的类
        LuceneDTO methodDirByClassName = luceneExe.getMethodDirByClassName(className);
        List listData =
                methodDirByClassName.getListData();
        AssertUtil.isFalse(CollectionUtils.isEmpty(listData), ErrorEnum.SELECT_INFO_NOT_FOUND.getName());
        MethodInfoDTO method = (MethodInfoDTO)listData.get(0);

        //遍历这个类下，和方法名匹配的方法
        String allClassName = method.getClassName();
        try {
            Class<?> aClass = Class.forName(allClassName);
            Method[] declaredMethods = aClass.getDeclaredMethods();

            //遍历所有方法，判断其名
            for(Method m:declaredMethods){
                if(m.getName().contains(methodName)){
                    MethodInfoDTO methodInfoDTO=new MethodInfoDTO();
                    methodInfoDTO.setClassName(allClassName);
                    methodInfoDTO.setMethodName(m.getName());
                    //入参列表
                    methodInfoDTO.setParams(StringUtils.join(getParams(m),","));
                    //出餐列表
                    methodInfoDTO.setReturnParams(m.getReturnType().getName());
                    result.add(methodInfoDTO);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DataResponse.of(result);
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

    private List<String> getParams(Method method){
        List<String> result=new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for(Class clazz:parameterTypes){
            result.add(clazz.getName());
        }
        return result;
    }
}
