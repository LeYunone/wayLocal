package com.leyunone.waylocal.service.impl.method;

import cn.hutool.core.collection.CollectionUtil;
import com.leyunone.waylocal.bean.vo.ClassInfoVO;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;
import com.leyunone.waylocal.service.HistoryService;
import com.leyunone.waylocal.service.LocationService;
import com.leyunone.waylocal.service.impl.search.SearchMethodServiceImpl;
import com.leyunone.waylocal.support.lucene.SearchCommandService;
import com.leyunone.waylocal.bean.dto.ClassDTO;
import com.leyunone.waylocal.bean.dto.LuceneDTO;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.response.DataResponse;
import com.leyunone.waylocal.util.UniqueSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author leyunone
 * @date 2022-02-21 13:59
 * 定位接口
 */
@Service
public class LocationServiceImpl implements LocationService {

    private final SearchMethodServiceImpl searchMethodService;
    private final HistoryService historyService;
    private final SearchCommandService searchCommandService;

    public LocationServiceImpl(SearchMethodServiceImpl searchMethodService, HistoryService historyService, SearchCommandService searchCommandService) {
        this.searchMethodService = searchMethodService;
        this.historyService = historyService;
        this.searchCommandService = searchCommandService;
    }

    /**
     * 只根据方法名模糊
     *
     * @param methodName
     * @return
     */
    public List<MethodInfoVO> getMethod(String methodName, Integer size) {
        List<MethodInfoVO> result = null;
        if (StringUtils.isBlank(methodName)) {
            List<MethodInfoVO> methodInfoVOS = historyService.readHistory();
            UniqueSet<String, MethodInfoVO> set = new UniqueSet<>(MethodInfoVO::getMethodName);
            set.addAll(methodInfoVOS);
            result = CollectionUtil.newArrayList(set);
        } else {
            result = searchCommandService.getMethodDir("methodName", methodName, size);
        }
        return result;
    }

    /**
     * 当类名清晰时， 类名+ 模糊方法名的组合查询搜索库
     *
     * @param className
     * @param method
     * @return
     */
    public List<MethodInfoVO> getMethod(String className, String method) {
        List<MethodInfoVO> result = searchCommandService.getMethodDirBooleanQuery(className, method);
        return result;
    }

    /**
     * 根据具体方法信息获取方法
     *
     * @param methodInfo
     * @return
     */
    public Method getMethod(MethodInfoDTO methodInfo) {
        Method method = searchMethodService.locationMethod(methodInfo);
        return method;
    }

    /**
     * 类名搜索
     *
     * @param className
     * @param size
     * @return
     */
    @Override
    public List<ClassInfoVO> getClassName(String className, Integer size) {
        List<ClassInfoVO> classDir = searchCommandService.getClassDir(className, size);
        return classDir;
    }

    /**
     * TODO 搜索优化
     * @param className
     * @param methodName
     * @return
     */
    public MethodInfoVO getOptimalMatch(String className, String methodName) {
        MethodInfoVO methodInfoVO = null;
        //如果两者都存在时
        List<MethodInfoVO> methodDirBooleanQuery = searchCommandService.getMethodDirBooleanQuery(className, methodName);
        if (!CollectionUtils.isEmpty(methodDirBooleanQuery)) {
            methodInfoVO = methodDirBooleanQuery.get(0);
        }
        return methodInfoVO;
    }
}
