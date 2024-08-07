package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.*;
import com.example.practice.mapper.*;
import com.example.practice.service.UserService;
import com.example.practice.service.impl.utils.UserDetailsImpl;
import com.example.practice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

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

    // 定义文章标签关系表操作变量
    @Autowired
    private ArticleTagMapper articleTagMapper;

    // 定义标签数据表操作变量
    @Autowired
    private TagMapper tagMapper;

    // 定义评论数据表操作变量
    @Autowired
    private CommentMapper commentMapper;

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
            redisTemplate.opsForValue().set("user:" + user.getUsername(), user, 1, TimeUnit.DAYS);
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
        String uploadPath = "D:/原桌面文件/学习笔记/实习/中午吃什么/blog-chsy_backend/src/main/resources/static/avatar/";
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
        System.out.println(user);
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
        queryWrapper.eq("author_id",id);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<Integer> commentCnt = new ArrayList<>();
        List<Integer> likeCnt = new ArrayList<>();
        List<List<String>> tagNames = new ArrayList<>();
        for (Article article : articles) {
            article.setContent(null);
            // 查询文章对应的点赞数和评论数以及标签
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("article_id", article.getId());
            articleLikeQueryWrapper.eq("article_id", article.getId());
            articleTagQueryWrapper.eq("article_id", article.getId());
            commentCnt.add(commentMapper.selectList(commentQueryWrapper).size());
            likeCnt.add(articleLikeMapper.selectList(articleLikeQueryWrapper).size());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
            List<String> stringList = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag.getTagId());
                stringList.add(tagMapper.selectOne(tagQueryWrapper).getTagName());
            }
            tagNames.add(stringList);
        }
        return Map.of("error_message", "success", "data", articles
                , "commentCnt", commentCnt, "likeCnt", likeCnt, "tagNames", tagNames);
    }

    @Override
    public Map<String, Object> getAllArticleNum(Integer id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id",id);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Map.of("error_message", "success", "data", articles.size());
    }

    // 获取用户文章的所有标签函数具体逻辑
    @Override
    public Map<String, Object> getAllTag(Integer id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<String> tagNames = new ArrayList<>();
        for (Article article : articles) {
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            articleTagQueryWrapper.eq("article_id", article.getId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag.getTagId());
                String tagName = tagMapper.selectOne(tagQueryWrapper).getTagName();
                if (!tagNames.contains(tagName)) {
                    tagNames.add(tagName);
                }
            }
        }
        return Map.of("error_message", "success", "data", tagNames);
    }

    @Override
    public Map<String, Object> getAllTagNum(Integer id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<String> tagNames = new ArrayList<>();
        for (Article article : articles) {
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            articleTagQueryWrapper.eq("article_id", article.getId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("id", articleTag.getTagId());
                String tagName = tagMapper.selectOne(tagQueryWrapper).getTagName();
                if (!tagNames.contains(tagName)) {
                    tagNames.add(tagName);
                }
            }
        }
        return Map.of("error_message", "success", "data", tagNames.size());
    }

    @Override
    public Map<String, Object> changeUserInfo(Integer id, String username, String phoneNumber, String gender, String avatar) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        if (username != null) {
            user.setUsername(username);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        if (gender != null) {
            user.setGender(gender);
        }
        String resourcePath = "src/main/resources/static/avatar/"; // 静态资源路径
        String resourceName = user.getAvatar(); // 资源文件名，根据实际情况更改
        if (resourceName != null) {
            File fileToDelete = new File(resourcePath + resourceName);
            if (!fileToDelete.delete()) {
                return Map.of("error_message", "头像修改失败");
            }
        }
        user.setAvatar(avatar);
        userMapper.update(user, queryWrapper);
        return Map.of("error_message", "success");
    }

    @Override
    public Map<String, Object> changeUserEmail(String oldEmail, String newEmail, String checkCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", oldEmail);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Map.of("error_message", "该邮箱未绑定用户，请重新输入");
        }
        String trueCode = redisTemplate.opsForValue().get("email:" + oldEmail).toString().substring(9, 15);
        if (trueCode == null) {
            return Map.of("error_message", "验证码过期,请重新输入");
        }
        if (trueCode.equals(checkCode)) {
            queryWrapper.clear();
            queryWrapper.eq("email",newEmail);
            if(userMapper.selectOne(queryWrapper)!=null){
                return Map.of("error_message", "新邮箱已绑定用户，请重新输入");
            }
            redisTemplate.delete("email:" + oldEmail);
            user.setEmail(newEmail);
            userMapper.update(user, queryWrapper);
            return Map.of("error_message", "success");
        } else {
            return Map.of("error_message", "验证码错误，请重新输入");
        }
    }

}