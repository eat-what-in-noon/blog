package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.Article;
import com.example.practice.entity.ArticleTag;
import com.example.practice.entity.Comment;
import com.example.practice.entity.Tag;
import com.example.practice.mapper.ArticleMapper;
import com.example.practice.mapper.ArticleTagMapper;
import com.example.practice.mapper.CommentMapper;
import com.example.practice.mapper.TagMapper;
import com.example.practice.service.ArticleService;
import com.example.practice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ArticleServiceImpl implements ArticleService {
    // 定义article数据库操作变量
    @Autowired
    private ArticleMapper articleMapper;

    // 定义tag数据库操作变量
    @Autowired
    private TagMapper tagMapper;

    // 定义article_tag数据表操作变量
    @Autowired
    private ArticleTagMapper articleTagMapper;

    // 定义comment数据表操作变量
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TagService tagService;

    // 添加文章函数具体逻辑
    @Override
    public Map<String, Object> addArticle(Article article) {
        articleMapper.insert(article);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getId); // 根据 id 字段降序排列
        Integer articleId = articleMapper.selectList(queryWrapper).get(0).getId();
        return Map.of("error_message", "success", "data", articleId);
    }

    // 为文章添加标签函数具体逻辑
    @Override
    public Map<String, Object> addTagToArticle(String tagName, Integer articleId) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Tag tag = tagMapper.selectOne(queryWrapper);
        if (tag == null) {
            tagService.addTag(tagName);
            queryWrapper.eq("tag_name", tagName);
            tag = tagMapper.selectOne(queryWrapper);
        }
        Integer tagId = tag.getId();
        ArticleTag articleTag = new ArticleTag(articleId, tagId);
        articleTagMapper.insert(articleTag);
        return Map.of("error_message", "success");
    }

    // 添加评论函数具体逻辑
    @Override
    public Map<String, Object> addComment(Comment comment) {
        commentMapper.insert(comment);
        return Map.of("error_message", "success");
    }

    // 上传文章封面函数具体逻辑
    @Override
    public Map<String, Object> uploadCover(MultipartFile cover) throws IOException {
        String uploadPath = "C:/Users/Joker/Desktop/JavaSpace/practice/src/main/resources/static/cover/";
        String originalFilename = cover.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        String savePath = uploadPath + fileName;
        File dest = new File(savePath);
        cover.transferTo(dest);
        return Map.of("error_message", "success", "data", fileName);
    }
}
