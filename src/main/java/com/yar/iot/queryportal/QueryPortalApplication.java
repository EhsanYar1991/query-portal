package com.yar.iot.queryportal;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class QueryPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueryPortalApplication.class, args);
    }
}
