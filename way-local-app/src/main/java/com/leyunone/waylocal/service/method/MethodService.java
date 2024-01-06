package com.leyunone.waylocal.service.method;

import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.command.InvokeMethodExe;
import com.leyunone.waylocal.constant.enums.ResolveHistoryTypeEnum;
import com.leyunone.waylocal.dto.MethodInfoDTO;
import com.leyunone.waylocal.response.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            List<MethodInfoDTO> dList = new ArrayList<>();
            dList.add(methodInfo);
            historyService.resolveHistory(ResolveHistoryTypeEnum.SAVE,dList);
        }catch (Exception e){
            logger.error("waylocal Error : "+e.getMessage());
        }
        return DataResponse.of(o);
    }
}
