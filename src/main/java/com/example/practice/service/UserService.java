package com.example.practice.service;

import com.example.practice.entity.ArticleLike;
import com.example.practice.entity.Follow;
import com.example.practice.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    // 密码登陆处理函数接口
    Map<String, Object> login(String loginName, String password);

    // 邮箱登陆处理函数接口
    Map<String, Object> loginByEmail(String email, String checkCode);

    // 注册处理函数接口
    Map<String, Object> register(User user);

    // 忘记密码处理函数接口
    Map<String, Object> forgetPassword(String email, String newPassword, String checkCode);

    // 判断验证码是否正确函数接口
    Map<String, Object> check(String email, String checkCode);

    // 上传头像处理函数接口
    Map<String, Object> uploadAvatar(MultipartFile avatar) throws IOException;

    // 注销处理函数接口
    Map<String, Object> logout(String username);

    // 用户信息查询处理函数接口
    Map<String, Object> userInfo(Integer id);

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

    // 获取用户写的所有文章接口
    Map<String, Object> getAllArticle(Integer id);

    // 获取用户文章的所有标签接口
    Map<String, Object> getAllTag(Integer id);
}
