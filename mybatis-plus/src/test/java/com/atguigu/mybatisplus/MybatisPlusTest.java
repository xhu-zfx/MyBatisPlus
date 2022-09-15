package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.mapper.UserMapper;
import com.atguigu.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList(){
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setName("张三");
        user.setAge(222);
        user.setEmail("zhangsan@github.com");
        int result = userMapper.insert(user);
        System.out.println(result);
        System.out.println(user.getId());
    }

    @Test
    public void testDelete(){
//        根据id进行删除
//        int result = userMapper.deleteById(1558790889986428929L);
//        根据map所设置的条件删除`deleteByMap`，map中存储的key为字段名，value为具体数据，多组k-v之间为AND关系
//        Map<String,Object> map=new HashMap<>();
//        map.put("id",1558790889986428929L);
//        map.put("name","张三");
//        int result = userMapper.deleteByMap(map);
        List<Long> userList = Arrays.asList(1L, 2L, 3L);
        int result = userMapper.deleteBatchIds(userList);
        System.out.println(result);
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setId(4L);
        user.setName("张我");
        user.setEmail("123@qq.com");
        int result = userMapper.updateById(user);
        System.out.println(result);
    }

    @Test
    public void testSelect(){
//        User user = userMapper.selectById(4L);
//        System.out.println(user);

//        List<Long> userList = Arrays.asList(1L, 2L, 3L);
//        List<User> users = userMapper.selectBatchIds(userList);
//        users.forEach(System.out::println);

//        Map<String,Object> map=new HashMap<>();
//        map.put("name","Jack");
//        map.put("age",20);
//        List<User> users = userMapper.selectByMap(map);
//        users.forEach(System.out::println);

        Map<String, Object> map = userMapper.selectMapById(1L);
        System.out.println(map);
    }




















}
