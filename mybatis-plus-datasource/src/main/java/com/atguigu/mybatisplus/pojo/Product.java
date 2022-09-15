package com.atguigu.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product")
public class Product {
    private Integer id;
    private String name;
    private Integer price;
    private Integer version;
}
