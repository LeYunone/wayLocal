package com.leyuna.waylocation.start;

import com.leyuna.waylocation.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengli
 * @date 2022-02-21
 */
@RestController
public class TestControl {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/test")
    public void test(){
        searchService.resoleAllMethod(this.getClass());
    }
    
    
}
