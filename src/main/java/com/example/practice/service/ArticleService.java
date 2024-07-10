package com.example.practice.service;

import com.example.practice.entity.Article;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ArticleService {
    // 添加文章函数接口
    Map<String, Object> addArticle(Article article);

    // 为文章添加标签函数接口
    Map<String, Object> addTagToArticle(String tagName, Integer articleId);

    // 上传文章封面函数接口
    Map<String, Object> uploadCover(MultipartFile cover) throws IOException;

    // 根据id获取文章信息函数接口
    Map<String, Object> getArticleInfoById(Integer id);

    // 根据tag获取文章信息函数接口
    Map<String, Object> getArticleInfoByTag(String tagName);

    // 根据category获取文章信息函数接口
    Map<String, Object> getArticleInfoByCategory(String categoryName);

    // 返回所有文章的content以外内容函数接口
    Map<String, Object> getAllArticle();

    // 获得文章所有评论的函数接口
    Map<String, Object> getComment(Integer id);

    // 查找文章函数接口
    Map<String, Object> findArticle(String title);

    // 判断用户是否给某篇文章点赞接口
    Boolean checkLike(Integer articleId, Integer userId);
}
