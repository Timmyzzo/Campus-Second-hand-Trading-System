package com.example.campustrade.mapper;

import com.example.campustrade.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper//声明是与数据库交互的 Mapper 接口
public interface UserMapper {
    //接口


    @Select("SELECT * FROM users WHERE username = #{username}")
        // #{username} 是一个占位符，MyBatis会自动把传入的 username 参数值填到这里，还能防止SQL注入
    User findByUsername(String username);


    @Insert("INSERT INTO users(username, password, gender, student_id) " +
            "VALUES(#{username}, #{password}, #{gender}, #{studentId})")
    void insert(User user);

    /**
     * 调用数据库存储过程 sp_get_users 来执行复杂查询。
     * @param username 用户名关键词
     * @param studentId 学号
     * @return 用户列表
     */
    @Select("CALL sp_get_users(#{username}, #{studentId})")
    @Options(statementType = StatementType.CALLABLE)//调用存储过程时，须明确告诉MyBatis,语句类型是可调用的
    List<User> findAll(String username, String studentId);
}