package com.leyuna.waylocation.config;

import com.leyuna.waylocation.service.search.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pengli
 * @create 2022-02-28 09:40
 */
@Component
public class EndDeleteFile implements DisposableBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SearchService searchService;

    @Override
    public void destroy () throws Exception {
        log.info("删除索引库文件中...");
        searchService.deleteMethodDirFile();
        log.info("删除成功");
    }
}
