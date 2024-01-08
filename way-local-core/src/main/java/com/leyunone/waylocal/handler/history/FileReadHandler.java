package com.leyunone.waylocal.handler.history;

import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.annotate.StrategyKey;
import com.leyunone.waylocal.bean.dto.MethodExcelDTO;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.common.ServerConstant;
import com.leyunone.waylocal.constant.global.WayLocalConstants;
import com.leyunone.waylocal.handler.factory.AbstractStrategyFactory;
import com.leyunone.waylocal.handler.factory.HistoryHandlerFactory;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
@StrategyKey(key = WayLocalConstants.FILE + "_READ")
@RequiredArgsConstructor
public class FileReadHandler extends HistoryReadHandler<MethodInfoDTO,Object> {

    private final HistoryHandlerFactory historyHandlerFactory;

    @Override
    public AbstractStrategyFactory<HistoryHandler> strategyFactory() {
        return historyHandlerFactory;
    }

    @Override
    public List<MethodInfoDTO> get() {
        List<MethodInfoDTO> result = new ArrayList<>();
        List<MethodExcelDTO> historyExcel = ServerConstant.historyExcel;
        historyExcel.stream().forEach(co ->{
            MethodInfoDTO methodInfoDTO = new MethodInfoDTO();
            methodInfoDTO.setClassName(co.getClassName());
            methodInfoDTO.setInvokeTime(co.getInvokeTime());
            methodInfoDTO.setMethodName(co.getMethodName());
            methodInfoDTO.setParamValue(JSONObject.parseArray(co.getParamValue(),String.class));
            methodInfoDTO.setReturnParamValue(co.getReturnParamValue());
            result.add(methodInfoDTO);
        });
        return result;
    }
}
