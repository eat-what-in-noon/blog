package com.example.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practice.entity.Tag;
import com.example.practice.mapper.TagMapper;
import com.example.practice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TagServiceImpl implements TagService {
    // 定义标签数据表操作变量
    @Autowired
    private TagMapper tagMapper;

    // 添加标签函数具体逻辑
    @Override
    public Map<String, Object> addTag(String tagName) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_name", tagName);
        Tag tag = tagMapper.selectOne(queryWrapper);
        if (tag != null) {
            return Map.of("error_message", "标签已存在");
        }
        tagMapper.insert(new Tag(null, tagName));
        return Map.of("error_message", "success");
    }
}
