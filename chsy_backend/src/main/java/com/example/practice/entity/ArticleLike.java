package com.example.practice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 文章点赞关系表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLike {
    private Integer userId;
    private Integer articleId;
}
