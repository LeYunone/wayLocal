package com.leyunone.waylocalsample;

import com.leyunone.waylocal.annotate.LocalEasyStart;
import com.leyunone.waylocal.annotate.MySqlConfig;
import com.leyunone.waylocal.easystart.QuickStartApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@LocalEasyStart(mysql = @MySqlConfig(url = "127.0.0.1:3306/test?seUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false"))
public class WayLocalSampleApplication {

    public static void main(String[] args) {
        QuickStartApplication.easyStart(WayLocalSampleApplication.class);
    }

}
