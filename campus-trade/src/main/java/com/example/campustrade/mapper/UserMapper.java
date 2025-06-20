package com.example.campustrade.mapper;

import com.example.campustrade.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface UserMapper {

    // 简单的查询和插入，继续使用注解
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users(username, password, gender, student_id) " +
            "VALUES(#{username}, #{password}, #{gender}, #{studentId})")
    void insert(User user);

    /**
     * 【最终解决方案】
     * 调用数据库存储过程 sp_get_users 来执行复杂查询。
     * @param username 用户名关键词
     * @param studentId 学号
     * @return 用户列表
     */
    @Select("CALL sp_get_users(#{username}, #{studentId})")
    @Options(statementType = StatementType.CALLABLE)
    List<User> findAll(String username, String studentId);
}