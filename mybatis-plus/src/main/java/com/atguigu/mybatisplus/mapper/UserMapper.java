package com.atguigu.mybatisplus.mapper;

import com.atguigu.mybatisplus.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    Map<String,Object> selectMapById(Long id);
}
