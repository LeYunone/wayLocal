package com.leyuna.waylocation.control;

import com.alibaba.fastjson.JSONObject;
import com.leyuna.waylocation.constant.global.ServerConstant;
import com.leyuna.waylocation.dto.ClassDTO;
import com.leyuna.waylocation.dto.LuceneDTO;
import com.leyuna.waylocation.response.DataResponse;
import com.leyuna.waylocation.service.method.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author pengli
 * @create 2022-03-02 14:45
 * 定位控制器
 */
@RestController
@RequestMapping("/location")
public class LocationControl {

    @Autowired
    private LocationService locationService;

    @RequestMapping("/getClassName")
    public DataResponse getClassName (String className,
                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        if (!StringUtils.isEmpty(className)) {
            //模糊查询类
            return locationService.getClassName(className, size);
        }
        //查找历史使用类
        LuceneDTO luceneDTO = new LuceneDTO();
        luceneDTO.setListData(this.getHistoryName(request));
        return DataResponse.of(luceneDTO);
    }

    @RequestMapping("/getMethod")
    public DataResponse getMethod (String className, String methodName, @RequestParam(required = false, defaultValue = "10") Integer size) {
        //如果没有指明类
        if (StringUtils.isEmpty(className)) {
            return locationService.getMethod(methodName, size);
        }
        try {
            //如果指明类非常清晰
            Class.forName(className);
            return locationService.getMethod(className, methodName, true);
        } catch (ClassNotFoundException e) {
            //如果指明类为模糊查询
            return locationService.getMethod(className, methodName, false);
        }
    }

    private List<ClassDTO> getHistoryName(HttpServletRequest request){
        switch(ServerConstant.saveType){
            case "cookie":
                return cookieClassName(request);
            case "object":
                return objectClassName();
            case "file":
                break;
            default:
                return null;
        }
        return null;
    }

    /**
     * 获得cookie中的className
     * @param request
     * @return
     */
    private List<ClassDTO> cookieClassName(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        List<ClassDTO> result = new ArrayList<>();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(ServerConstant.saveClass)){
                String value = cookie.getValue();
                List<String> strings = JSONObject.parseArray(value, String.class);
                for(String name:strings){
                    ClassDTO classDTO = new ClassDTO();
                    classDTO.setValue(name);
                    classDTO.setHightLineKey(name);
                    result.add(classDTO);
                }
            }
        }
        return result;
    }

    /**
     * 获得应用中的className
     * @return
     */
    private List<ClassDTO> objectClassName(){
        return new ArrayList<>(ServerConstant.historyClass);
    }
}
