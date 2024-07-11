package com.example.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practice.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

// 评论数据表操作接口
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
