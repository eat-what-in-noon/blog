package com.example.practice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.Article;
import com.example.practice.entity.Comment;
import com.example.practice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 文章相关操作接口
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // 添加文章接口
    // 接收参数为JSON，要求JSON中包含article表的除id外所有信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为新添加的文章id
    @PostMapping("/addArticle")
    public Map<String, Object> addArticle(@RequestBody Article article) {
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

    // 上传文章封面接口
    // 接收参数为Param，要求Param中必须包含上传的封面编码cover
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为封面文件在后端静态地址
    @GetMapping("/uploadCover")
    public Map<String, Object> uploadAvatar(@RequestParam("cover") MultipartFile cover) {
        if (cover.isEmpty()) {
            return Map.of("error_message", "cover is empty");
        }
        try {
            return articleService.uploadCover(cover);
        } catch (Exception e) {
            return Map.of("error_message", e.getMessage());
        }
    }

    // 根据id获取文章信息接口
    // 接收参数为Param，要求Param中包含文章id
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为id对应文章所有数据
    @GetMapping("/getArticleInfoById")
    public Map<String, Object> getArticleInfoById(@RequestParam String id) {
        return articleService.getArticleInfoById(id);
    }

    // 根据tag获取文章id和title接口
    // 接收参数为Param，要求Param中包含tag名tagName
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为有该tagName所有文章id和title
    @GetMapping("/getArticleInfoByTag")
    public Map<String, Object> getArticleInfoByTag(@RequestParam String tagName) {
        return articleService.getArticleInfoByTag(tagName);
    }

    // 获取所有文章id和title接口
    // 无接收参数
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为所有文章的id和title
    @GetMapping("/getAllArticle")
    public Map<String, Object> getAllArticle() {
        return articleService.getAllArticle();
    }

    // 获取所有文章所有评论
    // 接收参数为Param，要求Param中包含评论id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为所有评论所有属性
    @GetMapping("/getComment")
    public Map<String, Object> getComment(@RequestParam Integer id) {
        return articleService.getComment(id);
    }
}
