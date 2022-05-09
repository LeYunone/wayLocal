package com.leyuna.waylocation.control;

import com.leyuna.waylocation.response.DataResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengli
 * @date 2022-04-17
 */
@RequestMapping("/sql")
@RestController
public class SqlControl {

    /**
     * 回滚指定sql
     * @return
     */
    @RequestMapping("/sqlRollBack")
    public DataResponse sqlRollBack(String sql){
        return DataResponse.buildSuccess();
    }
}