package com.leyuna.waylocation.service.method;

import com.leyuna.waylocation.bean.dto.LuceneDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.command.LocationExe;
import com.leyuna.waylocation.command.LuceneExe;
import com.leyuna.waylocation.constant.enums.ErrorEnum;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.util.AssertUtil;
import com.leyuna.waylocation.util.StringResoleUtil;
import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @date  2022-02-21 13:59
 * 定位接口     
 */
@Service
public class LocationService {

    @Autowired
    private LuceneExe luceneExe;

    @Autowired
    private LocationExe locationExe;

    /**
     * 获得方法 最近使用的方法
     * @return DataResponse
     */
    public DataResponse getMethod(){
        return DataResponse.buildSuccess();
    }

    /**
     * 只根据方法名模糊
     * @param methodName
     * @return
     */
    public DataResponse getMethod(String methodName,Integer size){

        //默认走索引库搜索拿出最近十条匹配的数据展示
        LuceneDTO methodDirByMethodName = luceneExe.getMethodDir("methodName",methodName, size);
        return DataResponse.of(methodDirByMethodName.getListData());
    }

    /**
     * 当类名清晰时， 类名+ 模糊方法名的组合查询搜索库
     * @param className
     * @param method
     * @return
     */
    public DataResponse getMethod(String className,String method,boolean accuracy){
        List<MethodInfoDTO> result=null;
        if(accuracy){
            LuceneDTO methodDirBooleanQuery = luceneExe.getMethodDirBooleanQuery(className, method);
            result=methodDirBooleanQuery.getListData();
        }else{
            //先根据类名从搜索库查到最接近的类
            LuceneDTO methodDirByClassName = luceneExe.getClassDir(className,1);
            List<String> listData =
                    methodDirByClassName.getListData();
            if(CollectionUtils.isEmpty(listData)){
                return DataResponse.buildSuccess();
            }
            //最接近的类名
            String allClassName = listData.get(0);
            LuceneDTO methodDirBooleanQuery = luceneExe.getMethodDirBooleanQuery(allClassName, method);
            result = methodDirBooleanQuery.getListData();
        }
        return DataResponse.of(result);
    }

    /**
     * 根据具体方法信息获取方法
     * @param methodInfo
     * @return
     */
    public DataResponse<Method> getMethod(MethodInfoDTO methodInfo){
        try {
            Class<?> aClass = Class.forName(methodInfo.getClassName());
            String[] params = methodInfo.getParams().split(",");
            Class [] cs=new Class[params.length];
            for(int i=0;i<params.length;i++){
                cs[i]=Class.forName(params[i]);
            }
            Method method = null;
            String methodName = methodInfo.getMethodName();
            methodName = StringResoleUtil.replaceString(methodName, "<span style='color:red'>");
            methodName = StringResoleUtil.replaceString(methodName,"</span>");
            try {
                method = aClass.getMethod(methodName, cs);
            } catch (NoSuchMethodException e) {
                method = aClass.getDeclaredMethod(methodName,cs);
            }
            AssertUtil.isFalse(null==method,ErrorEnum.SELECT_INFO_NOT_FOUND.getName());
            return DataResponse.of(method);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return DataResponse.buildSuccess();
    }
    
    public DataResponse getClassName(String className,Integer size){
        LuceneDTO classDir = luceneExe.getClassDir(className, size);
        return DataResponse.of(classDir);
    }
}
