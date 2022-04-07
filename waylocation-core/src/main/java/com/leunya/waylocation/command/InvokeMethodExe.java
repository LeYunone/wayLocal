package com.leunya.waylocation.command;

import com.leunya.waylocation.dto.MethodInfoDTO;
import com.leunya.waylocation.service.InvokeDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengli
 * @create 2022-03-09 09:41
 */
@Service
public class InvokeMethodExe {

    @Autowired
    private InvokeDomainService invokeDomainService;

    /**
     * 调用方法
     * @param methodInfo
     * @return
     */
    public Object invokeMethod(MethodInfoDTO methodInfo){
        return invokeDomainService.invokeMethod(methodInfo);
    }
}
