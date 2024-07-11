package com.example.practice.service.impl;

import com.example.practice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements MailService {

    // 定义邮箱发送源 注解中为配置文件中的固定发送源
    @Value("${spring.mail.username}")
    private String from;

    // 定义邮箱发送体
    @Autowired
    private JavaMailSender mailSender;

    // 定义redis数据库存储变量
    @Autowired
    private RedisTemplate redisTemplate;

    // 发送不带附件邮件函数具体逻辑
    @Override
    public Map<String, Object> sendSimpleMail(String to, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
        //将验证码存入redis中，并设置只存在3分钟
        redisTemplate.opsForValue().set("email:" + to, content, 180, TimeUnit.SECONDS);
        return Map.of("error_message", "success");
    }

    @Override
    public boolean checkCode(String email, String code){
        String trueCode = redisTemplate.opsForValue().get("email:" + email).toString().substring(9, 15);
        return code.equals(trueCode);
    }
}
