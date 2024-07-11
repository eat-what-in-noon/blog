package com.example.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "D:/原桌面文件/学习笔记/实习/中午吃什么/blog-chsy_backend/src/main/resources/static/";
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + uploadPath);
        super.addResourceHandlers(registry);
    }
}
