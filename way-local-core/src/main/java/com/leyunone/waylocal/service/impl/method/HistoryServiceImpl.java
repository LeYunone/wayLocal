package com.leyunone.waylocal.service.impl.method;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.constant.enums.ResolveHistoryTypeEnum;
import com.leyunone.waylocal.common.ServerConstant;
import com.leyunone.waylocal.bean.dto.MethodExcelDTO;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2022-04-14 14:42
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private HttpServletResponse response;

    public void export(List<MethodInfoDTO> methodInfos){
        
        response.setHeader("Content-Disposition", "attachment;filename=export.xlsx");
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        try {
//            从HttpServletResponse中获取OutputStream输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //TODO 2022-4-14 等待扩展自定义文档功能
            List<MethodExcelDTO> exportData = new ArrayList<>();
            methodInfos.forEach(co ->{
                MethodExcelDTO methodExcelDTO = new MethodExcelDTO();
                methodExcelDTO.setClassName(co.getClassName());
                methodExcelDTO.setInvokeTime(co.getInvokeTime());
                methodExcelDTO.setMethodName(co.getMethodName());
                methodExcelDTO.setParamValue(JSONObject.toJSONString(co.getParamValue()));
                methodExcelDTO.setReturnParamValue(co.getReturnParamValue());
                exportData.add(methodExcelDTO);
            });
            
            EasyExcel.write(outputStream, MethodExcelDTO.class).sheet().doWrite(exportData);
        } catch (IOException e) {
            logger.error("waylocal : Error export Error");
        }
    }

    /**
     * 处理 [读取、保存、导出] 历史记录入口
     * @param historyTypeEnum
     * @param methodInfos
     * @return
     */
    public List<MethodInfoDTO> resolveHistory(ResolveHistoryTypeEnum historyTypeEnum,List<MethodInfoDTO> methodInfos){
    }
}
