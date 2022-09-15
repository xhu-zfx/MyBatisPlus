package com.atguigu.mybatisplus;


import com.atguigu.mybatisplus.mapper.UserMapper;
import com.atguigu.mybatisplus.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class MybatisPlusWrapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name","a")
                .between("age","20","30")
                .or()
                .isNull("email");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void test02(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("name")
                .orderByAsc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void test03(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.isNull("age");
        int res = userMapper.delete(queryWrapper);
        System.out.println(res);
    }

    @Test
    public void test04(){
//        将 (年龄大于20并且用户名中含有a) 或 邮箱为null 的用户信息修改
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.gt("age",20)
                .like("name","a")
                .or()
                .isNull("email");
        User user = new User();
        user.setName("小露");user.setEmail("222@qq.com");
        int res = userMapper.update(user,queryWrapper);
        System.out.println(res);
    }

    @Test
    public void test05(){
//        将 用户名中包含有a 且 (年龄大于20或邮箱为null) 的用户信息修改
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name","b")
                .and(i->i.gt("age",20).or().isNull("email"));
        User user = new User();
        user.setName("小红");user.setEmail("456@qq.com");
        userMapper.update(user,queryWrapper);
    }

    @Test void test06(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("name","age");
        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test void test07(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.inSql("id","select id from user where age>20");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test void test08(){
        UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
        updateWrapper.like("name","a")
                .and(i->i.gt("age",20)
                                .or()
                                .isNull("email")

                    );
        updateWrapper.set("name","xiaohei1").set("email","980@qq.com");
        userMapper.update(null,updateWrapper);
    }

    @Test void test09(){
        String username="";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){

        }
    }

    @Test void test10(){
        String username="";
        Integer ageBegin=20;
        Integer ageEnd=30;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username),"name",username)
                .ge(ageBegin!=null, "age",ageBegin)
                .le(ageEnd!=null,"age",ageEnd);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test void test11(){
        String username="";
        Integer ageBegin=20;
        Integer ageEnd=30;
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(username),User::getName,username)
                .ge(ageBegin!=null,User::getAge,ageBegin)
                .le(ageEnd!=null,User::getAge,ageEnd);
        List<User> userList = userMapper.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }
}
