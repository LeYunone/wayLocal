package com.leyunone.waylocal.control;

import com.alibaba.excel.EasyExcel;
import com.leyunone.waylocal.constant.enums.ResolveHistoryTypeEnum;
import com.leyunone.waylocal.constant.global.SqlInvokeConstant;
import com.leyunone.waylocal.dto.MethodInfoDTO;
import com.leyunone.waylocal.response.DataResponse;
import com.leyunone.waylocal.service.method.HistoryService;
import com.leyunone.waylocal.service.method.MethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author pengli
 * @create 2022-02-28 14:17
 * 方法相关控制器
 */
@RestController
@RequestMapping("/method")
public class MethodControl {

    @Autowired
    private MethodService methodService;

    @Autowired
    private HistoryService historyService;

    /**
     * 获得历史方法调用记录
     *
     * @return
     */
    @RequestMapping("/getHistory")
    public DataResponse getHistory () {
        return DataResponse.of(historyService.resolveHistory(ResolveHistoryTypeEnum.READ,null));
    }


    /**
     * 方法调用
     *
     * @param methodInfo
     * @return
     */
    @PostMapping("/invokeMethod")
    public DataResponse invokeMethod (@RequestBody MethodInfoDTO methodInfo) {
        if (SqlInvokeConstant.isGO) {
            return DataResponse.buildFailure("有一个数据库事务在进行");
        }
        //开启本次测试流程
        SqlInvokeConstant.isGO = true;
        //调用方法
        methodService.invokeMethod(methodInfo);
        methodInfo.setSqlInvokeDTO(SqlInvokeConstant.sqlInvokeDTO);
        //清空本次事务目录
        SqlInvokeConstant.sqlInvokeDTO = null;
        SqlInvokeConstant.isGO = false;
        return DataResponse.of(methodInfo);
    }

    @PostMapping("/export")
    public void export (@RequestBody List<MethodInfoDTO> methodInfoDTO) {
        historyService.export(methodInfoDTO);
    }
}
