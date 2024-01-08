package com.leyunone.waylocal.system.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.leyunone.waylocal.bean.dto.MethodExcelDTO;
import com.leyunone.waylocal.common.ServerConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author leyunone
 * @create 2022-04-13 14:48
 */
@Configuration
public class EasyExcelOrderListener extends AnalysisEventListener<MethodExcelDTO> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void invoke (MethodExcelDTO methodExcelDTO, AnalysisContext analysisContext) {
        ServerConstant.historyExcel.add(methodExcelDTO);
    }

    @Override
    public void doAfterAllAnalysed (AnalysisContext analysisContext) {
        logger.info("waylocal : success : 读取记录"+ServerConstant.historyExcel.size()+"条");
    }
}
