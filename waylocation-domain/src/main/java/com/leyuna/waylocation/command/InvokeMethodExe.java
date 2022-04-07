package com.leyuna.waylocation.command;

import com.leyuna.waylocation.bean.dto.MethodInfoDTO;
import com.leyuna.waylocation.bean.dto.SqlInvokeDTO;
import com.leyuna.waylocation.constant.global.SqlInvokeConstant;
import com.leyuna.waylocation.domainservice.InvokeDomainService;
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
