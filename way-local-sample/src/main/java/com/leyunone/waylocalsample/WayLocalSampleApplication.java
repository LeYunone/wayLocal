package com.leyunone.waylocalsample;

import com.leyunone.waylocal.annotate.LocalEasyStart;
import com.leyunone.waylocal.start.QuickStartApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@LocalEasyStart
public class WayLocalSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WayLocalSampleApplication.class,args);

//        QuickStartApplication.easyStart(WayLocalSampleApplication.class);
    }

}
