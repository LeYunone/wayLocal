package com.leyuna.waylocation.service.method;

import com.leyuna.waylocation.command.InvokeMethodExe;
import com.leyuna.waylocation.dto.MethodInfoDTO;
import com.leyuna.waylocation.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @create 2022-02-23 15:01
 * 调用方法服务
 */
@Service
public class InvokeMethodService {

    @Autowired
    private InvokeMethodExe invokeMethodExe;

    public DataResponse invokeMethod(MethodInfoDTO methodInfo){
        //执行方法
        return DataResponse.of(invokeMethodExe.invokeMethod(methodInfo));
    }
}
