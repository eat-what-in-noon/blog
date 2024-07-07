package com.example.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practice.entity.ArticleLike;
import org.apache.ibatis.annotations.Mapper;

// 文章点赞关系表操作接口
@Mapper
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {
}
