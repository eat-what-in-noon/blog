package com.example.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practice.entity.Article;
import org.apache.ibatis.annotations.Mapper;

// 文章数据表操作接口
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
