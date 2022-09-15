package com.atguigu.mybatisx.mapper;
import org.apache.ibatis.annotations.Param;

import com.atguigu.mybatisx.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author ZQ
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-08-16 15:54:55
* @Entity com.atguigu.mybatisx.pojo.User
*/
public interface UserMapper extends BaseMapper<User> {
    int insertSelective(User user);

    int deleteByIdAndAge(@Param("id") Long id, @Param("age") Integer age);

    int deleteByIdBetweenAndAgeBetweenOrAge(@Param("beginId") Long beginId, @Param("endId") Long endId, @Param("beginAge") Integer beginAge, @Param("endAge") Integer endAge, @Param("age") Integer age);

    int updateAgeAndSexById(@Param("age") Integer age, @Param("sex") Integer sex, @Param("id") Long id);
}





