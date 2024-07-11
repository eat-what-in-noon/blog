package com.example.practice.service;

import com.example.practice.entity.Tag;

import java.util.Map;

public interface TagService {

    // 添加标签函数接口
    Map<String, Object> addTag(String tagName);

    // 获取最热门十个标签接口
    Map<String, Object> getTag();
}
