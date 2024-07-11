package com.example.practice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 用户关注关系表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    private Integer followedId;
    private Integer followId;
}
