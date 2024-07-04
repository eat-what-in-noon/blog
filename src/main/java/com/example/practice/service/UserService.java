package com.example.practice.service;

import java.util.Map;

public interface UserService {

    // 登陆处理函数接口
    Map<String, Object> login(String username, String password);

    // 注册处理函数接口
    Map<String, Object> register(String username, String password);
}
