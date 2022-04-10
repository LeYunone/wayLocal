package com.leyuna.waylocation.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author pengli
 * @date 2022-04-10
 */
@Controller
@RequestMapping("/")
public class ViewControl {

    @GetMapping("/waylocation")
    public String htmlView(){
        return "waylocation";
    }
}
