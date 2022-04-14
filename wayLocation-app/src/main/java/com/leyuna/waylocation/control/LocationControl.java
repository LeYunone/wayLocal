package com.leyuna.waylocation.control;

import com.leyuna.waylocation.constant.enums.ResolveHistoryTypeEnum;
import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.LuceneDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.HistoryService;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private HistoryService historyService;

    @RequestMapping("/getClassName")
    public DataResponse getClassName (String className,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (!StringUtils.isEmpty(className)) {
            //模糊查询类
            return locationService.getClassName(className, size);
        }
        //查找历史使用类
        LuceneDTO luceneDTO = new LuceneDTO();
        luceneDTO.setListData(this.getHistoryName());
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

    private List<ClassDTO> getHistoryName(){
        List<MethodInfoDTO> list = historyService.resolveHistory(ResolveHistoryTypeEnum.READ, null);
        Set<String> collect = list.stream().map(MethodInfoDTO::getClassName).collect(Collectors.toSet());
        List<ClassDTO> result = new ArrayList<>();
        for(String name:collect){
            ClassDTO classDTO = new ClassDTO();
            classDTO.setValue(name);
            classDTO.setHightLineKey(name);
            result.add(classDTO);
        }
        return result;
    }
}
