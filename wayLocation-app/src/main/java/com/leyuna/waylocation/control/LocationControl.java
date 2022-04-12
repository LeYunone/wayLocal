package com.leyuna.waylocation.control;

import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.LuceneDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author pengli
 * @create 2022-03-02 14:45
 * 定位控制器
 */
@RestController
@RequestMapping("/location")
public class LocationControl {

    @Autowired
    private LocationService locationService;

    @RequestMapping("/getClassName")
    public DataResponse getClassName (String className,
                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        if (!StringUtils.isEmpty(className)) {
            //模糊查询类
            return locationService.getClassName(className, size);
        }
        LinkedHashMap<String, ClassDTO> classDTOS = new LinkedHashMap<>();

        //查找历史使用类
        LuceneDTO luceneDTO = new LuceneDTO();
        luceneDTO.setListData(this.getHistoryMethod(request));
        return DataResponse.of(luceneDTO);
    }

    @RequestMapping("/getMethod")
    public DataResponse getMethod (String className, String methodName, @RequestParam(required = false, defaultValue = "10") Integer size) {
        //如果没有指明类
        if (StringUtils.isEmpty(className)) {
            return locationService.getMethod(methodName, size);
        }
        try {
            //如果指明类非常清晰
            Class.forName(className);
            return locationService.getMethod(className, methodName, true);
        } catch (ClassNotFoundException e) {
            //如果指明类为模糊查询
            return locationService.getMethod(className, methodName, false);
        }
    }

    public List<ClassDTO> getHistoryMethod(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute("waylocation:class");
        List<ClassDTO> classDTOS = new ArrayList<>();
        if(null != attribute){
            List<String> strings = JSONObject.parseArray(attribute.toString(), String.class);
            for(String className:strings){
                ClassDTO classDTO=new ClassDTO();
                classDTO.setValue(className);
                classDTO.setHightLineKey(className);
                classDTOS.add(classDTO);
            }
        }
        return classDTOS;
    }
}
