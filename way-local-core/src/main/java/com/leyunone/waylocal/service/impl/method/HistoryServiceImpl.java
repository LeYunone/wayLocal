package com.leyunone.waylocal.service.impl.method;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;
import com.leyunone.waylocal.bean.dto.MethodExcelDTO;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.handler.factory.HistoryHandlerFactory;
import com.leyunone.waylocal.handler.history.HistoryHandler;
import com.leyunone.waylocal.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2022-04-14 14:42
 */
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HttpServletResponse response;
    private final HistoryHandlerFactory historyHandlerFactory;
    @Value("${waylocal.type:object}")
    private String resoleType;

    @Override
    public void export(List<MethodInfoDTO> methodInfos) {

        response.setHeader("Content-Disposition", "attachment;filename=export.xlsx");
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        try {
//            从HttpServletResponse中获取OutputStream输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //TODO 2022-4-14 等待扩展自定义文档功能
            List<MethodExcelDTO> exportData = new ArrayList<>();
            methodInfos.forEach(co -> {
                MethodExcelDTO methodExcelDTO = new MethodExcelDTO();
                methodExcelDTO.setClassName(co.getClassName());
                methodExcelDTO.setInvokeTime(co.getInvokeTime());
                methodExcelDTO.setMethodName(co.getMethodName());
                methodExcelDTO.setParamValue(JSONObject.toJSONString(co.getParamValue()));
                methodExcelDTO.setReturnParamValue(co.getReturnParamValue());
                exportData.add(methodExcelDTO);
            });
        } catch (IOException e) {
            logger.error("waylocal : Error export Error");
        }
    }

    /**
     * 处理 [读取、保存、导出] 历史记录入口
     *
     * @return
     */
    public List<MethodInfoVO> readHistory() {
        HistoryHandler strategyStore = historyHandlerFactory.getStrategyStore(resoleType + "_READ");
        List<MethodInfoVO> methodInfoVOS = new ArrayList<>();
        if (ObjectUtil.isNotNull(strategyStore)) {
            methodInfoVOS = strategyStore.get();
        }
        return methodInfoVOS;
    }

    @Override
    public void saveHistory(MethodInfoDTO methodInfoDTOS) {
        HistoryHandler strategyStore = historyHandlerFactory.getStrategyStore(resoleType + "_SAVE");
        strategyStore.save(methodInfoDTOS);
    }
}
