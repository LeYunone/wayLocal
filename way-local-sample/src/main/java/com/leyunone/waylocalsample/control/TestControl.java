package com.leyunone.waylocalsample.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.midi.Soundbank;

/**
 * :)
 *
 * @Author pengli
 * @Date 2024/1/26 15:44
 */
@RestController
@RequestMapping("/test")
public class TestControl {

    @Value("${spring.datasource.main.jdbc-url}")
    private String mysqlUrl;
    @Value("${spring.main.lazy-initialization}")
    private String laze;

    @RequestMapping("/syso")
    public void test(){
        System.out.println(mysqlUrl);
        System.out.println(laze);
    }
}
