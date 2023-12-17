package com.festu.blogify;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.festu.blogify.mapper")
@EnableConfigurationProperties
public class BlogifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogifyApplication.class, args);
    }

}
