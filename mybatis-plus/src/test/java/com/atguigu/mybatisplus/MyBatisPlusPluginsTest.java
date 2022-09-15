package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.mapper.ProductMapper;
import com.atguigu.mybatisplus.mapper.UserMapper;
import com.atguigu.mybatisplus.pojo.Product;
import com.atguigu.mybatisplus.pojo.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyBatisPlusPluginsTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test void test1(){
        Page<User> page = new Page<>(1,3);
        userMapper.selectPage(page,null);
        System.out.println("Records"+page.getRecords());
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
        System.out.println(page);
    }

    @Test void testProduct01(){

//        测试乐观锁与悲观锁

//        li查询
        Product productLi = productMapper.selectById(1);
        System.out.println("小李查询的价格"+productLi.getPrice());
//        wang查询
        Product productWang = productMapper.selectById(1);
        System.out.println("小王查询的价格"+productWang.getPrice());
//        li修改
        productLi.setPrice(productLi.getPrice()+50);
        productMapper.updateById(productLi);
//        wang修改
        productWang.setPrice(productWang.getPrice()-30);
        productMapper.updateById(productWang);

//        看看现在商品价格为多少
        Product product = productMapper.selectById(1);
        System.out.println("最终查询的价格"+product.getPrice());

//        最终价格为wang修改的价格 , 原因 : wang将li的修改结果覆盖
    }
}
