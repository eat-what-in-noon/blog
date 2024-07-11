package com.example.practice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 文章数据表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String createdDate;
    private Integer authorId;
    private Integer categoryId;
    private String cover;
    private String lastUpdateDate;
}
