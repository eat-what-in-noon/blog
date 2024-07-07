package com.example.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practice.entity.Category;
import org.apache.ibatis.annotations.Mapper;

// 分类数据表操作接口
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
