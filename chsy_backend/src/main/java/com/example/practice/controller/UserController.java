package com.example.practice.controller;

import com.example.practice.entity.ArticleLike;
import com.example.practice.entity.Follow;
import com.example.practice.entity.User;
import com.example.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.PublicKey;
import java.util.Map;

//用户相关操作接口
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 用户密码登陆接口
    // 接收参数为JSON，要求JSON中包含username和password
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data是当前登陆的用户数据
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return userService.login(username, password);
    }

    // 用户邮箱登陆接口
    // 接收参数为JSON，要求JSON中包含email和checkCode
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data是当前登陆的用户数据
    @PostMapping("/loginByEmail")
    public Map<String, Object> loginByEmail(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String checkCode = map.get("checkCode");
        return userService.loginByEmail(email, checkCode);
    }

    // 用户注册接口
    // 接收参数为JSON，要求JSON中至少包含username、password和email
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        return userService.register(user);
    }

    // 用户忘记密码接口
    // 接收参数为JSON，要求JSON中必须包含用户的email、忘记密码之后要修改的新密码newPassword以及邮箱验证码checkCode
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PutMapping("/forgetPassword")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String newPassword = map.get("newPassword");
        String checkCode = map.get("checkCode");
        return userService.forgetPassword(email, newPassword, checkCode);
    }

    // 判断验证码是否正确接口
    // 接收参数为JSON，要求JSON中必须包含用户的email以及邮箱验证码checkCode
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/checkCode")
    public Map<String, Object> check(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String checkCode = map.get("checkCode");
        return userService.check(email, checkCode);
    }

    // 上传头像接口
    // 接收参数为Param，要求Param中必须包含上传的头像编码avatar，avatar中仅包含静态资源名，不包含路径。
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为头像文件在后端静态地址
    @PostMapping("/uploadAvatar")
    public Map<String, Object> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {
        if (avatar.isEmpty()) {
            return Map.of("error_message", "avatar is empty");
        }
        try {
            return userService.uploadAvatar(avatar);
        } catch (Exception e) {
            return Map.of("error_message", e.getMessage());
        }
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
    // 接收参数为Param，要求包含用户id
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为当前登陆的用户数据
    @GetMapping("/userinfo")
    public Map<String, Object> userinfo(@RequestParam Integer id) {
        return userService.userInfo(id);
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

    // 获取用户所有文章除content外内容接口
    // 接收参数为Param，要求Param中包含用户id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为用户所有文章的id和title的List
    @GetMapping("/getAllArticle")
    public Map<String, Object> getAllArticle(@RequestParam Integer id) {
        return userService.getAllArticle(id);
    }

    // 获取用户所写文章的数量的接口
    // 接收参数为Param，要求Param中包含用户id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为用户所写文章的数量
    @GetMapping("/getAllArticleNum")
    public Map<String,Object> getAllArticleNum(@RequestParam Integer id){
        return userService.getAllArticleNum(id);
    }

    // 获取用户文章的所有标签接口
    // 接收参数为Param，要求Param中包含用户id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为用户所有使用过的所有tag的tagName
    @GetMapping("/getAllTag")
    public Map<String, Object> getAllTag(@RequestParam Integer id) {
        return userService.getAllTag(id);
    }

    // 获取用户所用标签的数量的接口
    // 接收参数为Param，要求Param中包含用户id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为用户所用标签的数量
    @GetMapping("/getAllTagNum")
    public Map<String, Object> getAllTagNum(@RequestParam Integer id) {
        return userService.getAllTagNum(id);
    }

    // 修改用户普通信息接口
    // 接收参数为JSON，要求JSON中包含用户修改的新信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PutMapping("/changeUserInfo")
    public Map<String, Object> changeUserInfo(@RequestBody Map<String, String> map) {
        Integer id = Integer.valueOf(map.get("id"));
        String username = map.get("username");
        String phoneNumber = map.get("phoneNumber");
        String gender = map.get("gender");
        String avatar = map.get("avatar");
        return userService.changeUserInfo(id, username, phoneNumber, gender, avatar);
    }

    // 修改用户邮箱接口
    // 接收参数为JSON，要求JSON中包含用户旧邮箱，新邮箱，验证码
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PutMapping("/changeUserEmail")
    public Map<String, Object> changeUserEmail(@RequestBody Map<String, String> map) {
        String oldEmail = map.get("oldEmail");
        String newEmail = map.get("newEmail");
        String checkCode = map.get("checkCode");
        return userService.changeUserEmail(oldEmail, newEmail, checkCode);
    }
}