package com.leyunone.waylocal.control;

import com.leyunone.waylocal.bean.query.ClassQuery;
import com.leyunone.waylocal.bean.vo.ClassInfoVO;
import com.leyunone.waylocal.bean.response.DataResponse;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;
import com.leyunone.waylocal.service.HistoryService;
import com.leyunone.waylocal.service.LocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author leyunone
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

    /**
     * 类名框搜索定位
     *
     * @return
     */
    @GetMapping("/getClassName")
    public DataResponse<List<ClassInfoVO>> getClassName(ClassQuery query) {
        List<ClassInfoVO> result = null;
        if (StringUtils.isNotBlank(query.getClassName())) {
            //模糊查询类
            result = locationService.getClassName(query.getClassName(), query.getPageSize());
        } else {
            result = this.getHistoryName();
        }
        return DataResponse.of(result);
    }

    /**
     * 方法框搜索定位
     *
     * @return
     */
    @GetMapping("/getMethod")
    public DataResponse<List<MethodInfoVO>> getMethod(ClassQuery query) {
        List<MethodInfoVO> result = null;
        String className = query.getClassName();
        String methodName = query.getMethodName();
        //如果没有指明类
        if (StringUtils.isBlank(className)) {
            result = locationService.getMethod(methodName, query.getPageSize());
        } else {
            try {
                //如果指明类非常清晰
                Class.forName(className);
                result = locationService.getMethod(className, methodName);
            } catch (ClassNotFoundException e) {
                //TODO 类名错误
            }
        }
        return DataResponse.of(result);
    }

    /**
     * 最佳匹配搜索定位
     *
     * @return
     */
    @GetMapping("/getOptimalMatch")
    public DataResponse<MethodInfoVO> getOptimalMatch(ClassQuery query) {
        MethodInfoVO optimalMatch = locationService.getOptimalMatch(query.getClassName(), query.getMethodName());
        return DataResponse.of(optimalMatch);
    }

    private List<ClassInfoVO> getHistoryName() {
        List<MethodInfoVO> list = historyService.readHistory();
        Set<String> collect = list.stream().map(MethodInfoVO::getClassName).collect(Collectors.toSet());
        List<ClassInfoVO> result = new ArrayList<>();
        for (String name : collect) {
            ClassInfoVO classInfoVO = new ClassInfoVO();
            classInfoVO.setValue(name);
            classInfoVO.setHightLineKey(name);
            result.add(classInfoVO);
        }
        return result;
    }
}
