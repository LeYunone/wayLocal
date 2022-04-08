package com.leyuna.waylocation.config;

import com.leyuna.waylocation.service.search.SearchLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author pengli
 * @create 2022-02-28 09:36
 */
@Component
public class ClazzOrderRunner implements ApplicationRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SearchLibraryService searchService;

    @Override
    public void run (ApplicationArguments args) throws Exception {
        log.info("构造搜索库文件中....");
        searchService.resoleAllMethod(this.getClass());
        log.info("构造成功");
    }
}
