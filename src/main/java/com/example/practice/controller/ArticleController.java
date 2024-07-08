package com.example.practice.controller;

import com.example.practice.entity.Article;
import com.example.practice.entity.Comment;
import com.example.practice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// 文章相关操作接口
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // 添加文章接口
    // 接收参数为JSON，要求JSON中包含article表的除id外所有信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/addArticle")
    public Map<String, Object> addArticle(@RequestBody Article article) {
        System.out.println(article);
        return articleService.addArticle(article);
    }

    // 为文章添加标签接口
    // 接收参数为JSON，要求JSON中包含标签名和文章id
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/addTagToArticle")
    public Map<String, Object> addTagToArticle(@RequestBody Map<String, String> map) {
        String tagName = map.get("tagName");
        Integer articleId = Integer.valueOf(map.get("articleId"));
        return articleService.addTagToArticle(tagName, articleId);
    }

    // 添加评论接口
    // 接收参数为JSON，要求JSON中包含comment表除id外所有信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/addComment")
    public Map<String, Object> addComment(@RequestBody Comment comment) {
        return articleService.addComment(comment);
    }
}