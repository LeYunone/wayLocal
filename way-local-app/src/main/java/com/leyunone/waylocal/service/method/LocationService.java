package com.leyunone.waylocal.service.method;

import com.leyunone.waylocal.command.ClassExe;
import com.leyunone.waylocal.command.LocationExe;
import com.leyunone.waylocal.command.WaylocalLuceneExe;
import com.leyunone.waylocal.constant.enums.ResolveHistoryTypeEnum;
import com.leyunone.waylocal.dto.ClassDTO;
import com.leyunone.waylocal.dto.LuceneDTO;
import com.leyunone.waylocal.dto.MethodInfoDTO;
import com.leyunone.waylocal.response.DataResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author pengli
 * @date 2022-02-21 13:59
 * 定位接口
 */
@Service
public class LocationService {

    @Autowired
    private WaylocalLuceneExe luceneExe;

    @Autowired
    private LocationExe locationExe;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ClassExe classExe;

    /**
     * 只根据方法名模糊
     *
     * @param methodName
     * @return
     */
    public DataResponse getMethod(String methodName, Integer size) {
        if(StringUtils.isBlank(methodName)){
            List<MethodInfoDTO> methodInfoDTOS = historyService.resolveHistory(ResolveHistoryTypeEnum.READ, null);
            Set<MethodInfoDTO> set= new TreeSet(new Comparator<MethodInfoDTO>() {
                @Override
                public int compare(MethodInfoDTO o1, MethodInfoDTO o2) { 
                    return o1.getMethodName().compareTo(o2.getMethodName());
                }
            });
            set.addAll(methodInfoDTOS);
            return DataResponse.of(new ArrayList(set));
        }
        //默认走索引库搜索拿出最近十条匹配的数据展示
        LuceneDTO methodDirByMethodName = luceneExe.getMethodDir("methodName", methodName, size);
        return DataResponse.of(methodDirByMethodName.getListData());
    }

    /**
     * 当类名清晰时， 类名+ 模糊方法名的组合查询搜索库
     *
     * @param className
     * @param method
     * @return
     */
    public DataResponse getMethod(String className, String method, boolean accuracy) {
        List<MethodInfoDTO> result = null;
        LuceneDTO luceneDTO = null;
        if (accuracy) {
            if (StringUtils.isBlank(method)) {
                result = classExe.getMethodInfoInClass(className);
            } else {
                luceneDTO = luceneExe.getMethodDirBooleanQuery(className, method);
                result = luceneDTO.getListData();
            }
        } else {
            //先根据类名从搜索库查到最接近的类
            luceneDTO = luceneExe.getClassDir(className, 1);
            List<ClassDTO> listData =
                    luceneDTO.getListData();
            if (CollectionUtils.isEmpty(listData)) {
                return DataResponse.buildSuccess();
            }
            //最接近的类名
            ClassDTO classDTO = listData.get(0);
            className = classDTO.getValue();
            if (StringUtils.isBlank(method)) {
                //如果方法名为空，则展示该内中所有方法
                result = classExe.getMethodInfoInClass(className);
            } else {
                luceneDTO = luceneExe.getMethodDirBooleanQuery(className, method);
                result = luceneDTO.getListData();
            }
        }
        return DataResponse.of(result);
    }

    /**
     * 根据具体方法信息获取方法
     *
     * @param methodInfo
     * @return
     */
    public DataResponse<Method> getMethod(MethodInfoDTO methodInfo) {
        return DataResponse.of(locationExe.locationMethod(methodInfo));
    }

    /**
     * 类名搜索
     *
     * @param className
     * @param size
     * @return
     */
    public DataResponse getClassName(String className, Integer size) {
        return DataResponse.of(luceneExe.getClassDir(className, size));
    }

    public DataResponse getOptimalMatch(String className, String methodName) {

        if (StringUtils.isBlank(className) && StringUtils.isBlank(methodName)) {
            //如果类名和方法名都是空时
            List<MethodInfoDTO> methodInfoDTOS = historyService.resolveHistory(ResolveHistoryTypeEnum.READ, null);
            if (!CollectionUtils.isEmpty(methodInfoDTOS)) {
                return DataResponse.of(methodInfoDTOS.get(0));
            }
        } else if (StringUtils.isBlank(className)) {
            //如果类名为空时
            LuceneDTO methodDirByMethodName = luceneExe.getMethodDir("methodName", methodName, 1);
            List listData = methodDirByMethodName.getListData();
            if (!CollectionUtils.isEmpty(listData)) {
                return DataResponse.of(listData.get(0));
            }
        } else if (StringUtils.isBlank(methodName)) {
            //如果方法名为空时
            List<MethodInfoDTO> methodInfoInClass = classExe.getMethodInfoInClass(className);
            if (!CollectionUtils.isEmpty(methodInfoInClass)) {
                return DataResponse.of(methodInfoInClass.get(0));
            }
        } else {
            //如果两者都存在时
            LuceneDTO methodDirBooleanQuery = luceneExe.getMethodDirBooleanQuery(className, methodName);
            List listData = methodDirBooleanQuery.getListData();
            if (!CollectionUtils.isEmpty(listData)) {
                return DataResponse.of(listData.get(0));
            }
        }
        return DataResponse.buildFailure("竟然没有找到最优解");
    }
}
