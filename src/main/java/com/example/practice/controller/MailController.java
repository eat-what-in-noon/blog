package com.example.practice.controller;


import com.example.practice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    // 发送邮件接口
    // 接收参数为Param，要求Param中包含需要修改密码用户的邮箱email
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @GetMapping("/getCheckCode")
    public Map<String, Object> getCheckCode(@RequestParam String email) {
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的注册验证码为：" + checkCode + ",有效时间3分钟";
        try {
            return mailService.sendSimpleMail(email, "注册验证码", message);
        } catch (Exception e) {
            return Map.of("error_message", "邮件发送失败");
        }
    }
}
