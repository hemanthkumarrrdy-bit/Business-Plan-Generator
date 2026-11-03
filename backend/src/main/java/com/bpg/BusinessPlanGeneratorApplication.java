package com.bpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BusinessPlanGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessPlanGeneratorApplication.class, args);
    }
}
