package com.leyunone.waylocal.control;

import com.leyunone.waylocal.bean.response.DataResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leyunone
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
