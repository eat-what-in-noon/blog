package com.example.practice.service;

import java.util.Map;

public interface MailService {

    // 发送不带附件邮件函数接口
    public Map<String, Object> sendSimpleMail(String to, String title, String content);

    boolean checkCode(String email, String code);
}
