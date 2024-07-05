package com.example.practice.service;

import com.example.practice.entity.User;

import java.util.Map;

public interface UserService {

    // 登陆处理函数接口
    Map<String, Object> login(String loginName, String password);

    // 注册处理函数接口
    Map<String, Object> register(User user);

    // 注销处理函数接口
    Map<String, Object> logout(String username);
    
    // 用户信息查询处理函数接口
    Map<String, Object> userInfo();
}
