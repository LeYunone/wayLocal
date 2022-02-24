package com.leyuna.waylocation.control;

import com.leyuna.waylocation.bean.dto.LuceneDTO;
import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.command.AnalyzerTest;
import com.leyuna.waylocation.config.SpringContextUtil;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.param.ParamService;
import com.leyuna.waylocation.service.search.SearchService;
import com.leyuna.waylocation.service.way.LocationMethodService;
import com.leyuna.waylocation.util.StringResoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author pengli
 * @date 2022-02-21
 */
@RestController
public class TestControl {

    @Autowired
    private SearchService searchService;

    @Autowired
    private LocationMethodService wayService;

    @Autowired
    private ParamService paramService;

    /**
     * 生成搜索库
     */
    @RequestMapping("/test")
    public void test(){
        searchService.resoleAllMethod();
    }

    /**
     * 测试定位方法准度
     * @param test
     */
    @RequestMapping("/test2")
    public void test2(String test){
        wayService.getMethod(test,10);
    }

    /**
     * 测试分词器
     * @throws Exception
     */
    @RequestMapping("/test3")
    public void test3() throws Exception {
        AnalyzerTest analyzerTest=new AnalyzerTest();
        analyzerTest.myAnalyzerTest();
    }

    /**
     * 自动装载对象解决方案
     * @throws Exception
     */
    @RequestMapping("/test4")
    public void test4() throws Exception {
        Class<?> aClass = Class.forName("com.leyuna.waylocation.service.search.SearchService");
        Method resoleAllMethod =
                aClass.getMethod("resoleAllMethod");
        Object bean = SpringContextUtil.getBean(aClass);
        resoleAllMethod.invoke(bean);
    }

    /**
     * 测试方法定位到得到方法入参结构流程
     * @param method
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    @RequestMapping("/test5")
    public DataResponse test5(String method) throws ClassNotFoundException, NoSuchMethodException {
        DataResponse data = wayService.getMethod(method, 1);
        LuceneDTO l=(LuceneDTO)data.getData();
        List<MethodInfoDTO> list=l.getListData();
        MethodInfoDTO methodInfoDTO = list.get(0);
        String className = methodInfoDTO.getClassName();
        Class<?> aClass = Class.forName(className);
        String[] params = methodInfoDTO.getParams().split(",");
        Class [] cs=new Class[params.length];
        for(int i=0;i<params.length;i++){
            cs[i]=Class.forName(params[i]);
        }
        String methodName = methodInfoDTO.getMethodName();
        methodName = StringResoleUtil.replaceString(methodName, "<span style='color:red'>");
        methodName = StringResoleUtil.replaceString(methodName,"</span>");
        Method method1=null;
        try {
            method1 = aClass.getMethod(methodName, cs);
        }catch (Exception e){
            method1 = aClass.getDeclaredMethod(methodName,cs);
        }
        return paramService.getParam(method1);
    }
    
}
