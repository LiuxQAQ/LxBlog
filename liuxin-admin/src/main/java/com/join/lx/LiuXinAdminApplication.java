package com.join.lx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.join.lx.mapper")
@EnableSwagger2
public class LiuXinAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiuXinAdminApplication.class,args);
    }
}
