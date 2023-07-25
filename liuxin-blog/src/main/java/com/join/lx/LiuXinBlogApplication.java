package com.join.lx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.join.lx.mapper")
@EnableScheduling  // 开启定时任务功能
@EnableSwagger2
public class LiuXinBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiuXinBlogApplication.class,args);
    }
}
