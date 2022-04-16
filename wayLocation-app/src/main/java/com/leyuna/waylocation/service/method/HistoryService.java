package com.leyuna.waylocation.service.method;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.constant.enums.ResolveHistoryTypeEnum;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.dto.MethodExcelDTO;
import com.leyuna.waylocation.dto.MethodInfoDTO;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2022-04-14 14:42
 */
@Service
public class HistoryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private HttpServletRequest request;

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
            logger.error("Waylocation : Error export Error");
        }
    }

    /**
     * 处理 [读取、保存、导出] 历史记录入口
     * @param historyTypeEnum
     * @param methodInfos
     * @return
     */
    public List<MethodInfoDTO> resolveHistory(ResolveHistoryTypeEnum historyTypeEnum,List<MethodInfoDTO> methodInfos){
        switch (ServerConstant.saveType){
            case "cookie":
                switch (historyTypeEnum){
                    case READ:
                        return this.getHistoryToCookie();
                    case SAVE:
                        this.saveHistoryToCookie(methodInfos);
                    default:
                        break;
                }
                break;
            case "object":
                switch (historyTypeEnum){
                    case READ:
                        return this.getHistoryToObject();
                    case SAVE:
                        this.saveHistoryToObject(methodInfos);
                    default:
                        break;
                }
                break;
            case "file":
                switch (historyTypeEnum){
                    case READ:
                        return this.getHistoryToFile();
                    case SAVE:
                        this.saveHistoryToFile(methodInfos);
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 调用记录存储到cookie中
     * @param
     */
    private void saveHistoryToCookie(List<MethodInfoDTO> methodInfos) {
        Cookie[] cookies = request.getCookies();
        //记录集合
        List<MethodInfoDTO> methodList = new ArrayList<>();
        if(null != cookies){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(ServerConstant.saveMethod)){
                    //去之前存储的记录集合
                    String value = cookie.getValue();
                    methodList = JSONObject.parseArray(value,MethodInfoDTO.class);
                }
            }
        }
        //存储记录
        for(MethodInfoDTO methodInfo:methodInfos){
            MethodInfoDTO methodInfoDTO = new MethodInfoDTO();
            methodInfoDTO.setClassName(methodInfo.getClassName());
            methodInfoDTO.setMethodName(methodInfo.getMethodName());
            methodList.add(methodInfoDTO);
        }

        Cookie methodCookie = new Cookie(ServerConstant.saveMethod,
                JSONObject.toJSONString(methodList));

        //保存cookie
        response.addCookie(methodCookie);
    }


    /**
     * 保存全部信息到 应用中
     */
    private void saveHistoryToObject( List<MethodInfoDTO> methodInfos){

        //存储本次调用记录
        methodInfos.forEach(co->co.setInvokeTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        ServerConstant.historyMethod.addAll(methodInfos);
    }

    private void saveHistoryToFile (List<MethodInfoDTO> methodInfos) {
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

        logger.info("waylocation Success : file记录保存成功" );
    }

    private List<MethodInfoDTO> getHistoryToCookie(){
        Cookie[] cookies = request.getCookies();
        List<MethodInfoDTO> result = new ArrayList<>();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(ServerConstant.historyMethod)){
                String value = cookie.getValue();
                return JSONObject.parseArray(value, MethodInfoDTO.class);
            }
        }
        return result;
    }

    private List<MethodInfoDTO> getHistoryToObject(){
        return ServerConstant.historyMethod;
    }

    private List<MethodInfoDTO> getHistoryToFile(){
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
