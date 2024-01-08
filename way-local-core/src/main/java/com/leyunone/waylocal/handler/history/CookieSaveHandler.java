package com.leyunone.waylocal.handler.history;

import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.annotate.StrategyKey;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.common.ServerConstant;
import com.leyunone.waylocal.constant.enums.OperationEnum;
import com.leyunone.waylocal.constant.global.WayLocalConstants;
import com.leyunone.waylocal.handler.StrategyAutoRegisterComponent;
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
@StrategyKey(key = WayLocalConstants.COOKIE + "_SAVE")
@RequiredArgsConstructor
public class CookieSaveHandler extends HistorySaveHandler<Object,List<MethodInfoDTO>> {

    private final HistoryHandlerFactory historyHandlerFactory;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public AbstractStrategyFactory<HistoryHandler> strategyFactory() {
        return historyHandlerFactory;
    }

    @Override
    public void save(List<MethodInfoDTO> methodInfos) {
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
}
