package com.example.practice.service;

import com.example.practice.entity.ArticleLike;
import com.example.practice.entity.Follow;
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

    // 用户点赞函数接口
    Map<String, Object> like(ArticleLike articleLike);

    // 用户取消点赞函数接口
    Map<String, Object> cancelLike(ArticleLike articleLike);

    // 用户关注函数接口
    Map<String, Object> follow(Follow follow);

    // 用户取消关注函数接口
    Map<String, Object> cancelFollow(Follow follow);

    // 获取用户被点赞数量接口
    Map<String, Object> getLikeNum(Integer id);

    // 获取用户关注人接口
    Map<String, Object> getFollowNum(Integer id);
}
