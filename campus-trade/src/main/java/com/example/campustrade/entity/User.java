package com.example.campustrade.entity;

import lombok.Data; // 引入Lombok
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data // Lombok注解, 自动生成getter, setter, toString等方法
public class User {
    private Integer id;
    private String username;
    private String password;
    private String gender;
    private String studentId; // 驼峰命名法
    private BigDecimal balance;
    private Timestamp createdAt;

}
