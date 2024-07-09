package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.*;
import com.example.practice.mapper.ArticleLikeMapper;
import com.example.practice.mapper.ArticleMapper;
import com.example.practice.mapper.FollowMapper;
import com.example.practice.mapper.UserMapper;
import com.example.practice.service.UserService;
import com.example.practice.service.impl.utils.UserDetailsImpl;
import com.example.practice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    // 定义redis数据库存储变量
    @Autowired
    private RedisTemplate redisTemplate;

    // 定义文章点赞关系表操作变量
    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    // 定义用户关注关系表操作变量
    @Autowired
    private FollowMapper followMapper;

    // 定义文章数据表操作变量
    @Autowired
    private ArticleMapper articleMapper;

    // 密码登陆处理函数具体逻辑
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
            User user1 = (User) redisTemplate.opsForValue().get("user:" + user.getUsername());
            if (user1 != null) {
                return Map.of("error_message", "用户已登陆,请勿重复登陆");
            }
            redisTemplate.opsForValue().set("user:" + user.getUsername(), user, 1, TimeUnit.DAYS);
            return Map.of("error_message", "success", "token", jwt, "data", user);
        } catch (Exception e) {
            return Map.of("error_message", "用户名或密码错误");
        }
    }

    // 邮箱登陆处理函数具体逻辑
    @Override
    public Map<String, Object> loginByEmail(String email, String checkCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Map.of("error_message", "用户不存在");
        }
        String trueCode = redisTemplate.opsForValue().get("email:" + email).toString().substring(9, 15);
        if (trueCode == null) {
            return Map.of("error_message", "验证码过期,请重新输入");
        }
        if (trueCode.equals(checkCode)) {
            redisTemplate.delete("email:" + email);

            // 生成 JWT token
            String jwt = JwtUtil.createJWT(user.getId().toString());
            return Map.of("error_message", "success", "token", jwt, "data", user);
        } else {
            return Map.of("error_message", "验证码错误，请重新输入");
        }
    }

    //注册处理函数具体逻辑
    @Override
    public Map<String, Object> register(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        if (username == null) {
            return Map.of("error_message", "用户名不能为空");
        }
        username = username.trim();
        if (username.length() == 0) {
            return Map.of("error_message", "用户名不能为空");
        }
        if (password == null) {
            return Map.of("error_message", "密码不能为空");
        }
        password = password.trim();
        if (password.length() == 0) {
            return Map.of("error_message", "密码不能为空");
        }
        if (email == null) {
            return Map.of("error_message", "邮箱不能为空");
        }
        email = email.trim();
        if (email.length() == 0) {
            return Map.of("error_message", "邮箱不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            return Map.of("error_message", "该邮箱已绑定用户，请切换邮箱");
        }
        String EncodedPassword = passwordEncoder.encode(password);
        user.setPassword(EncodedPassword);
        userMapper.insert(user);
        return Map.of("error_message", "success");
    }

    // 忘记密码处理函数具体逻辑
    @Override
    public Map<String, Object> forgetPassword(String email, String newPassword, String checkCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Map.of("error_message", "该邮箱未绑定用户，请重新输入");
        }
        String trueCode = redisTemplate.opsForValue().get("email:" + email).toString().substring(9, 15);
        if (trueCode == null) {
            return Map.of("error_message", "验证码过期,请重新输入");
        }
        if (trueCode.equals(checkCode)) {
            String EncodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(EncodedPassword);
            userMapper.update(user, queryWrapper);
            redisTemplate.delete("email:" + email);
            return Map.of("error_message", "success");
        } else {
            return Map.of("error_message", "验证码错误，请重新输入");
        }

    }

    // 判断验证码是否正确函数具体逻辑
    @Override
    public Map<String, Object> check(String email, String checkCode) {
        String trueCode = redisTemplate.opsForValue().get("email:" + email).toString().substring(9, 15);
        if (trueCode == null) {
            return Map.of("error_message", "验证码过期,请重新输入");
        }
        if (trueCode.equals(checkCode)) {
            redisTemplate.delete("email:" + email);
            return Map.of("error_message", "success");
        } else {
            return Map.of("error_message", "验证码错误，请重新输入");
        }
    }

    // 上传头像处理函数具体逻辑
    @Override
    public Map<String, Object> uploadAvatar(MultipartFile avatar) throws IOException {
        String uploadPath = "C:/Users/Joker/Desktop/JavaSpace/practice/src/main/resources/static/avatar/";
        String originalFilename = avatar.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        String savePath = uploadPath + fileName;
        File dest = new File(savePath);
        avatar.transferTo(dest);
        return Map.of("error_message", "success", "data", fileName);
    }

    // 注销处理函数具体逻辑
    @Override
    public Map<String, Object> logout(String username) {
        if (username == null) {
            return Map.of("error_message", "用户名不能为空");
        }
        if (username.length() == 0) {
            return Map.of("error_message", "用户名不能为空");
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + username);
        if (user == null) {
            return Map.of("error_message", "用户未登陆");
        }
        redisTemplate.delete("user:" + username);
        return Map.of("error_message", "success");
    }

    // 用户信息获取处理函数具体逻辑
    @Override
    public Map<String, Object> userInfo(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        return Map.of("error_message", "success", "data", user);
    }

    @Override
    public Map<String, Object> like(ArticleLike articleLike) {
        articleLikeMapper.insert(articleLike);
        return Map.of("error_message", "success");
    }

    @Override
    public Map<String, Object> cancelLike(ArticleLike articleLike) {
        Integer userId = articleLike.getUserId();
        Integer articleId = articleLike.getArticleId();
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("article_id", articleId);
        articleLikeMapper.delete(queryWrapper);
        return Map.of("error_message", "success");
    }

    @Override
    public Map<String, Object> follow(Follow follow) {
        followMapper.insert(follow);
        return Map.of("error_message", "success");
    }

    @Override
    public Map<String, Object> cancelFollow(Follow follow) {
        Integer followedId = follow.getFollowedId();
        Integer followId = follow.getFollowId();
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followed_id", followedId).eq("follow_id", followId);
        followMapper.delete(queryWrapper);
        return Map.of("error_message", "success");
    }

    @Override
    public Map<String, Object> getLikeNum(Integer id) {
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        List<ArticleLike> articleLikes = articleLikeMapper.selectList(queryWrapper);
        int cnt = 0;
        for (ArticleLike articleLike : articleLikes) {
            Integer articleId = articleLike.getArticleId();
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("id", articleId).eq("author_id", id);
            Article article = articleMapper.selectOne(articleQueryWrapper);
            if (article != null) {
                cnt++;
            }
        }
        return Map.of("error_message", "success", "data", cnt);
    }

    @Override
    public Map<String, Object> getFollowNum(Integer id) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follow_id", id);
        List<Follow> follows = followMapper.selectList(queryWrapper);
        return Map.of("error_message", "success", "data", follows.size());

    }

    // 获取用户写过的所有文章除了content外内容函数具体逻辑
    @Override
    public Map<String, Object> getAllArticle(Integer id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        List<Article> articles = articleMapper.selectList(queryWrapper);
        for (Article article : articles) {
            if (Objects.equals(article.getAuthorId(), id)) {
                article.setContent(null);
            }
        }
        return Map.of("error_message", "success", "data", articles);
    }

}
