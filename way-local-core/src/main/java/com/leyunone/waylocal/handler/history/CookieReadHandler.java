package com.leyunone.waylocal.handler.history;

import com.alibaba.fastjson.JSONObject;
import com.leyunone.waylocal.annotate.StrategyKey;
import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;
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
@StrategyKey(key = WayLocalConstants.COOKIE + "_READ")
@RequiredArgsConstructor
public class CookieReadHandler extends HistoryReadHandler<MethodInfoVO,Object> {

    private final HistoryHandlerFactory historyHandlerFactory;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public AbstractStrategyFactory<HistoryHandler> strategyFactory() {
        return historyHandlerFactory;
    }

    @Override
    public List<MethodInfoVO> get() {
        Cookie[] cookies = request.getCookies();
        List<MethodInfoVO> result = new ArrayList<>();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(ServerConstant.historyMethod)){
                String value = cookie.getValue();
                return JSONObject.parseArray(value, MethodInfoVO.class);
            }
        }
        return result;
    }
}
