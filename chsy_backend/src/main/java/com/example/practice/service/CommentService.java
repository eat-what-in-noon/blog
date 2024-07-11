package com.example.practice.service;

import com.example.practice.entity.Comment;

import java.util.Map;

public interface CommentService {
    // 评论删除函数接口
    Map<String, Object> deleteComment(Integer id);

    // 添加评论函数接口
    Map<String, Object> addComment(Comment comment);
}
