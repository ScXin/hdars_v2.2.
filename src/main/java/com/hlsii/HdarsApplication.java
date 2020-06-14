package com.hlsii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ScXin
 * @date 5/8/2020 1:41 PM
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class HdarsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HdarsApplication.class, args);
    }
}
