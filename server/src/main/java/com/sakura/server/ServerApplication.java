package com.sakura.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Server模块Spring Boot主类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.sakura.server", "com.sakura.worker"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
