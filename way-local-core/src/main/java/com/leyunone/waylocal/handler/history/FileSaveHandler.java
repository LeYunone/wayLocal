package com.leyunone.waylocal.handler.history;

import com.alibaba.excel.EasyExcel;
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
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/9
 */
@StrategyKey(key = WayLocalConstants.FILE + "_SAVE")
@RequiredArgsConstructor
public class FileSaveHandler extends HistorySaveHandler<Object,List<MethodInfoDTO>> {

    private final HistoryHandlerFactory historyHandlerFactory;

    @Override
    public AbstractStrategyFactory<HistoryHandler> strategyFactory() {
        return historyHandlerFactory;
    }

    @Override
    public void save(List<MethodInfoDTO> methodInfos) {
        File file = new File(ServerConstant.savePath + "/history.xlsx");
        for(MethodInfoDTO methodInfo:methodInfos){
            MethodExcelDTO excelDTO = new MethodExcelDTO();
            excelDTO.setClassName(methodInfo.getClassName());
            excelDTO.setInvokeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            excelDTO.setMethodName(methodInfo.getMethodName());
            excelDTO.setParamValue(JSONObject.toJSONString(methodInfo.getParamValue()));
            excelDTO.setReturnParamValue(methodInfo.getReturnParamValue());
            ServerConstant.historyExcel.add(excelDTO);
        }

        //写文档
        EasyExcel.write(file, MethodExcelDTO.class).sheet().doWrite(ServerConstant.historyExcel);
    }
}
