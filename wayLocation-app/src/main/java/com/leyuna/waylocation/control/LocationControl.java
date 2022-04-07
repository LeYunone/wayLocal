package com.leyuna.waylocation.control;

import com.alibaba.fastjson.JSONObject;
import com.leunya.waylocation.dto.ClassDTO;
import com.leunya.waylocation.dto.LuceneDTO;
import com.leunya.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                      @CookieValue(value = "historyClass", required = false) String historyClass) {
        if (!StringUtils.isEmpty(className)) {
            //模糊查询类
            return locationService.getClassName(className, size);
        }
        LinkedHashMap<String, ClassDTO> classDTOS = new LinkedHashMap<>();

        //查找历史使用类
        if (StringUtils.isEmpty(historyClass)) {
            return DataResponse.of(new LuceneDTO());
        }
        classDTOS = JSONObject.parseObject(historyClass, classDTOS.getClass());
        LuceneDTO luceneDTO = new LuceneDTO();
        luceneDTO.setListData(new LinkedList<>(classDTOS.values()));
        luceneDTO.setTotole(classDTOS.size());
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
}
