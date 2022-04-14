package com.leyuna.waylocation.service.method;

import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.command.InvokeMethodExe;
import com.leyuna.waylocation.constant.enums.ResolveHistoryTypeEnum;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @create 2022-02-23 15:01
 * 调用方法服务
 */
@Service
public class MethodService {

    @Autowired
    private InvokeMethodExe invokeMethodExe;

    @Autowired
    private HistoryService historyService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 开启方法
     * @param methodInfo
     * @return
     */
    public DataResponse invokeMethod(MethodInfoDTO methodInfo){
        //执行方法
        Object o = null;
        try {
            o = invokeMethodExe.invokeMethod(methodInfo);
            methodInfo.setReturnParamValue(o==null?"": JSONObject.toJSONString(o));
            //历史保存任务
            historyService.resolveHistory(ResolveHistoryTypeEnum.SAVE,methodInfo);
        }catch (Exception e){
            logger.error("Waylocation Error : "+e.getMessage());
        }
        return DataResponse.of(o);
    }
}
