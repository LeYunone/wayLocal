package com.leyuna.waylocation.autoconfig;

import com.alibaba.excel.EasyExcel;
import com.leyuna.waylocation.config.EasyExcelOrderListener;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.dto.MethodExcelDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * @author pengli
 * @date 2022-04-07
 */
@Configuration()
@ComponentScan({"com.leyuna.waylocation"})
@EnableConfigurationProperties(WayLocationProperties.class)
public class WaylocationAutoConfiguration {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public WaylocationAutoConfiguration(WayLocationProperties wayLocationProperties){
        //保存类型，由用户决定保存方式：session cookie file
        String saveType = wayLocationProperties.getSaveType();
        String savePath = wayLocationProperties.getSavePath();
        solveSave(saveType,savePath);
    }

    /**
     * 处理保存方式
     * @param saveType
     */
    private void solveSave(String saveType,String savePath){
        if(StringUtils.isNotBlank(saveType)){
            ServerConstant.saveType = saveType;
            if(saveType.equals("object")){
                //初始化站内记录
                ServerConstant.historyClass = new HashSet<>();
                ServerConstant.historyMethod = new Stack<>();
            }else if(saveType.equals("file")){
                //file
                ServerConstant.historyExcel = new ArrayList<>();
                solveFileSave(savePath);
            }else{
                //cookie
            }
        }else{
            //默认为cookie保存
            ServerConstant.saveType = "object";
            //初始化站内记录
            ServerConstant.historyClass = new HashSet<>();
            ServerConstant.historyMethod = new Stack<>();
        }
    }

    /**
     * 处理file方式的保存路径
     * @param savePath
     */
    private void solveFileSave(String savePath){
        if(StringUtils.isNotBlank(savePath)){
            ServerConstant.savePath = savePath;
        }else{
            ServerConstant.savePath = "C:/waylocation/";
        }
        
        //创建文件夹
        File file = new File(ServerConstant.savePath);
        if(!file.exists()){
            boolean mkdirs = file.mkdirs();
            if(mkdirs){
                logger.info("waylocation Success : file - 文件夹创建成功 路径: "+ServerConstant.savePath);
            }
        }else{
            logger.info("waylocation Success : file - 文件夹创建成功 路径: "+ServerConstant.savePath);
        }

        //读取历史文件
        String fileName = ServerConstant.savePath+"/history.xlsx";
        File history = new File(fileName);
        if(!history.exists()){
            try {
                history.createNewFile();
            } catch (IOException e) {
                logger.error("waylocation Error : create file fail");
            }
        }
        if(history.length()!=0){
            //读取文档的同时，组装新文档
            EasyExcel.read(fileName, MethodExcelDTO.class,new EasyExcelOrderListener()).sheet().doRead();
        }
    }
}
