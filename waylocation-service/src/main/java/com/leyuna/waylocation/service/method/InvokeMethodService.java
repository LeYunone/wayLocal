package com.leyuna.waylocation.service.method;

import com.leunya.waylocation.command.InvokeMethodExe;
import com.leunya.waylocation.dto.MethodInfoDTO;
import com.leunya.waylocation.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

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
