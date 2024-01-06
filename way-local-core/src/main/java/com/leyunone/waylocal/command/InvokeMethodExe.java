package com.leyunone.waylocal.command;

import com.leyunone.waylocal.dto.MethodInfoDTO;
import com.leyunone.waylocal.domainservice.InvokeDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author leyunone
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
