package com.example.practice.controller;

import com.example.practice.entity.ArticleLike;
import com.example.practice.entity.Follow;
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

    // 用户点赞接口
    // 接收参数为JSON，要求JSON中包含article_like表中所有数据
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/like")
    public Map<String, Object> like(@RequestBody ArticleLike articleLike) {
        return userService.like(articleLike);
    }

    // 用户取消点赞接口
    // 接收参数为JSON，要求JSON中包含article_like表中所有数据
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @DeleteMapping("/cancelLike")
    public Map<String, Object> cancelLike(@RequestBody ArticleLike articleLike) {
        return userService.cancelLike(articleLike);
    }

    // 用户关注接口
    // 接收参数为JSON，要求JSON中包含follow表中所有数据
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/follow")
    public Map<String, Object> follow(@RequestBody Follow follow) {
        return userService.follow(follow);
    }

    // 用户取消关注接口
    // 接收参数为JSON，要求JSON中包含follow表中所有数据
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @DeleteMapping("/cancelFollow")
    public Map<String, Object> cancelFollow(@RequestBody Follow follow) {
        return userService.cancelFollow(follow);
    }

    // 用户获取被点赞数接口
    // 接收参数为Param，要求Param中包含用户id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为用户被点赞数
    @GetMapping("/getLikeNum")
    public Map<String, Object> getLikeNum(@RequestParam Integer id) {
        return userService.getLikeNum(id);
    }

    // 用户获取关注数接口
    // 接收参数为Param，要求Param中包含用户id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为用户关注数
    @GetMapping("/getFollowNum")
    public Map<String, Object> getFollowNum(@RequestParam Integer id) {
        return userService.getFollowNum(id);
    }
}
