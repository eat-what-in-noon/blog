package com.example.practice.controller;

import com.example.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//用户相关操作接口
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 用户登陆接口
    // 接收参数为JSON，要求JSON中包含username和password
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data是当前登陆的用户数据
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return userService.login(username, password);
    }

    // 用户注册接口
    // 接收参数为JSON，要求JSON中包含username和password
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return userService.register(username, password);
    }
}
