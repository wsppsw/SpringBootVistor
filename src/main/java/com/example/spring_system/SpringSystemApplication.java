package com.example.spring_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages ="com.example.spring_system.Filter")
public class SpringSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSystemApplication.class, args);
    }

}
