package com.leyunone.waylocal.service.impl.method;

import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.response.DataResponse;
import com.leyunone.waylocal.service.HistoryService;
import com.leyunone.waylocal.service.MethodService;
import com.leyunone.waylocal.service.impl.invoke.InvokeDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2022-02-23 15:01
 * 调用方法服务
 */
@Service
public class MethodServiceImpl implements MethodService {

    private final HistoryService historyService;
    private final InvokeDomainService invokeDomainService;

    public MethodServiceImpl(HistoryService historyService, InvokeDomainService invokeDomainService) {
        this.historyService = historyService;
        this.invokeDomainService = invokeDomainService;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 开启方法
     *
     * @param methodInfo
     * @return
     */
    public DataResponse invokeMethod(MethodInfoDTO methodInfo) {
        //执行方法
        Object o = null;
        try {
            o = invokeDomainService.invokeMethod(methodInfo);
            methodInfo.setReturnParamValue(o == null ? "" : JSONObject.toJSONString(o));
            //历史保存任务
            List<MethodInfoDTO> dList = new ArrayList<>();
            dList.add(methodInfo);
            historyService.saveHistory(methodInfo);
        } catch (Exception e) {
            logger.error("waylocal Error : " + e.getMessage());
        }
        return DataResponse.of(o);
    }
}
