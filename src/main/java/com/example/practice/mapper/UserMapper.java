package com.example.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practice.entity.User;
import org.apache.ibatis.annotations.Mapper;

//用户数据库操作接口
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
