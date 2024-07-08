package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.Comment;
import com.example.practice.mapper.CommentMapper;
import com.example.practice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    // 评论删除函数具体逻辑
    @Override
    public Map<String, Object> deleteComment(Integer id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        commentMapper.delete(queryWrapper);
        return Map.of("error_message", "success");
    }

    // 评论添加函数具体逻辑
    @Override
    public Map<String, Object> addComment(Comment comment) {
        commentMapper.insert(comment);
        return Map.of("error_message", "success", "data", comment.getId());
    }
}
