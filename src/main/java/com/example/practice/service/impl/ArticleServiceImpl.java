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
import java.util.*;

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

    // 根据id获取文章信息函数具体逻辑
    @Override
    public Map<String, Object> getArticleInfoById(String id) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Article article = articleMapper.selectOne(queryWrapper);
        return Map.of("error_message", "success", "data", article);
    }

    // 根据tag获取所有相关文章的除content外所有内容
    @Override
    public Map<String, Object> getArticleInfoByTag(String tagName) {
        // 根据tagName查询tagId
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Tag tag = tagMapper.selectOne(queryWrapper);
        Integer tagId = tag.getId();
        // 根据tagId查询articleId
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("tag_id", tagId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQueryWrapper);
        // 遍历articleId，返回所有相关文章
        List<Article> articles = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("id", articleTag.getArticleId());
            Article article = articleMapper.selectOne(articleQueryWrapper);
            // 此时不需要content，为了节省开销，将content置为null
            article.setContent(null);
            articles.add(article);
        }
        return Map.of("error_message", "success", "data", articles);
    }

    // 返回所有文章的除了content外内容函数具体逻辑
    @Override
    public Map<String, Object> getAllArticle() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        List<Article> articles = articleMapper.selectList(queryWrapper);
        for (Article article : articles) {
            article.setContent(null);
        }
        return Map.of("error_message", "success", "data", articles);
    }

    // 获得文章所有评论的函数具体逻辑
    @Override
    public Map<String, Object> getComment(Integer id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        if (comments == null) {
            return Map.of("error_message", "该文章暂时没有评论");
        }
        for (Comment comment : comments) {
            if (!Objects.equals(comment.getArticleId(), id)) {
                comments.remove(comment);
            }
        }
        return Map.of("error_message", "success", "data", comments);
    }


}
