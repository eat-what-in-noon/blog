package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.User;
import com.example.practice.mapper.UserMapper;
import com.example.practice.service.UserService;
import com.example.practice.service.impl.utils.UserDetailsImpl;
import com.example.practice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    //定义数据库操作变量
    @Autowired
    private UserMapper userMapper;

    //定义token生成变量
    @Resource
    private AuthenticationManager authenticationManager;

    // 定义密码加密变量
    @Autowired
    private PasswordEncoder passwordEncoder;

    //登陆处理函数具体逻辑
    @Override
    public Map<String, Object> login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 若合法，则将其取出并赋予用户UserDetailsImpl类中的各种属性，形成loginUser
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

            // 单独取loginUser中的用户信息，用于生成JWT-token
            User user = loginUser.getUser();
            String jwt = JwtUtil.createJWT(user.getId().toString());

            // 将生成的jwt-token传回给controller层

            return Map.of("error_message", "success", "token", jwt);
        } catch (Exception e) {
            return Map.of("error_message", "账号不存在，请先注册一个账号");

        }
    }

    //注册处理函数具体逻辑
    @Override
    public Map<String, Object> register(String username, String password) {
        if (username == null) {
            return Map.of("error_message", "用户名不能为空");
        }
        if (username.length() == 0) {
            return Map.of("error_message", "用户名不能为空");
        }
        if (password == null) {
            return Map.of("error_message", "密码不能为空");
        }
        if (password.length() == 0) {
            return Map.of("error_message", "密码不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            return Map.of("error_message", "用户名已存在");
        }
        String EncodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, EncodedPassword, "0", null, null, null, null, null, null);
        userMapper.insert(user);
        return Map.of("error_message", "success");
    }

}
