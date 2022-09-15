package com.atguigu.mybatisplus.pojo;

import com.atguigu.mybatisplus.enums.SexEnum;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private SexEnum sex;
    @TableLogic
    public Integer is_deleted;
}
