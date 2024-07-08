package com.example.practice.service;

import com.example.practice.entity.Article;
import com.example.practice.entity.Comment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ArticleService {
    // 添加文章函数接口
    Map<String, Object> addArticle(Article article);

    // 为文章添加标签函数接口
    Map<String, Object> addTagToArticle(String tagName, Integer articleId);

    // 添加评论函数接口
    Map<String, Object> addComment(Comment comment);

    // 上传文章封面函数接口
    Map<String, Object> uploadCover(MultipartFile cover) throws IOException;

    // 根据id获取文章信息函数接口
    Map<String, Object> getArticleInfoById(String id);

    // 根据tag获取文章信息函数接口
    Map<String, Object> getArticleInfoByTag(String tagName);

    // 返回所有文章的id和title函数接口
    Map<String, Object> getAllArticle();
}
