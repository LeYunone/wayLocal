package com.leyuna.waylocation.start;

import com.leyuna.waylocation.command.AnalyzerTest;
import com.leyuna.waylocation.service.search.SearchService;
import com.leyuna.waylocation.service.way.LocationWayService;
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

    @Autowired
    private LocationWayService wayService;

    @RequestMapping("/test")
    public void test(){
        searchService.resoleAllMethod(this.getClass());
    }

    @RequestMapping("/test2")
    public void test2(){
        wayService.getWay("getW",10);
    }

    @RequestMapping("/test3")
    public void test3() throws Exception {
        AnalyzerTest analyzerTest=new AnalyzerTest();
        analyzerTest.StandardAnalyzerTest();
    }
    
}
