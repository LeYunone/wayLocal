package com.leyunone.waylocalsample.control.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/30
 */
@Service
public class TestService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("==============11");
    }
}
