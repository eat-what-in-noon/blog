package com.example.practice.controller;

import com.example.practice.entity.Comment;
import com.example.practice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // 添加评论接口
    // 接收参数为JSON，要求JSON中包含comment表除id外所有信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。data为新加评论的id
    @PostMapping("/addComment")
    public Map<String, Object> addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    // 删除评论接口
    // 接收参数为JSON，要求JSON中包含对应评论的id信息
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @DeleteMapping("/deleteComment")
    public Map<String, Object> deleteComment(@RequestBody Map<String, String> map) {
        Integer id = Integer.valueOf(map.get("id"));
        return commentService.deleteComment(id);
    }
}
