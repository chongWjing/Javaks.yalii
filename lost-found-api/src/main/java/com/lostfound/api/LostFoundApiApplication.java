package com.lostfound.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 失物招领管理系统 REST API
 */
@SpringBootApplication
public class LostFoundApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(LostFoundApiApplication.class, args);
        System.out.println("========================================");
        System.out.println("  失物招领管理系统 API 已启动");
        System.out.println("  后端API: http://localhost:8080");
        System.out.println("  前端页面: http://localhost:3000");
        System.out.println("========================================");
    }
}
