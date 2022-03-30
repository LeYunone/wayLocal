package com.leyuna.waylocation.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.leyuna.waylocation"})
public class WaylocationStartApplication {

    public static void main (String[] args) {
        SpringApplication.run(WaylocationStartApplication.class, args);
    }

}
