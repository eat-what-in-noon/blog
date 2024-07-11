package com.example.practice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 分类数据表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String categoryName;
}
