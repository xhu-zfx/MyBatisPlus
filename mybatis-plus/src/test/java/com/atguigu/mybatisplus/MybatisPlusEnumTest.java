package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.enums.SexEnum;
import com.atguigu.mybatisplus.mapper.UserMapper;
import com.atguigu.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MybatisPlusEnumTest {
    @Autowired
    private UserMapper userMapper;

    @Test void test(){
        User user = new User();
        user.setName("包");user.setAge(22);user.setSex(SexEnum.MALE);
        userMapper.insert(user);
    }
}
