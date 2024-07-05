package com.example.practice.controller;

import com.example.practice.entity.User;
import com.example.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//用户相关操作接口
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 用户登陆接口
    // 接收参数为JSON，要求JSON中包含username和password或email和password
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data是当前登陆的用户数据
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String email = map.get("email");
        String loginName;
        if (username == null) {
            loginName = email;
        } else {
            loginName = username;
        }
        return userService.login(loginName, password);
    }

    // 用户注册接口
    // 接收参数为JSON，要求JSON中至少包含username、password和email
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        return userService.register(user);
    }

    // 用户注销接口
    // 接收参数为JSON，要求JSON中包含username
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        return userService.logout(username);
    }

    // 用户信息接口
    // 无接收参数
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为当前登陆的用户数据
    @GetMapping("/userinfo")
    public Map<String, Object> userinfo() {
        return userService.userInfo();
    }
}
