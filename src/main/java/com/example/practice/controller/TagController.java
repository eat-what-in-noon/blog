package com.example.practice.controller;

import com.example.practice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 标签操作相关接口
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    // 添加标签接口
    // 接收参数为JSON，要求JSON中包含标签名tagName
    // 返回参数为JSON，其中error_message为提示信息，正常运行时为success；发生错误时则是对应错误。无data
    @PostMapping("/addTag")
    public Map<String, Object> addTag(@RequestBody Map<String, String> map) {
        String tagName = map.get("tagName");
        return tagService.addTag(tagName);
    }

    @GetMapping("/getTag")
    public Map<String, Object> getTag() {
        return tagService.getTag();
    }
}
