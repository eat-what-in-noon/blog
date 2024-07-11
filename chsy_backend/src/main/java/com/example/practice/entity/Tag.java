package com.example.practice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 标签数据表实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String tagName;
}
