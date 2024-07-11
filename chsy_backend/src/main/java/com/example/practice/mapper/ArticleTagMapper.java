package com.example.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practice.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

// 文章标签关系表操作接口
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
}
