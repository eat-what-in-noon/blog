package com.example.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "C:/Users/Joker/Desktop/JavaSpace/practice/src/main/resources/static/";
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + uploadPath);
        super.addResourceHandlers(registry);
    }
}
