package com.example.practice.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    // 添加文章函数具体逻辑
    @Override
    public Map<String, Object> addArticle(Article article) {
        articleMapper.insert(article);
        return Map.of("error_message", "success");
    }

    // 为文章添加标签函数具体逻辑
    @Override
    public Map<String, Object> addTagToArticle(String tagName, Integer articleId) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Tag tag = tagMapper.selectOne(queryWrapper);
        if (tag == null) {
            Map.of("error_message", "该标签不存在，请创建该标签或重新选择标签");
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
}
