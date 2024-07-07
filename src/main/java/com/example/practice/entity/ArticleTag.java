package com.example.practice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 文章标签关系表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag {
    private Integer articleId;
    private Integer tagId;
}
