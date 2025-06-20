package com.example.campustrade.entity;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Message {
    private Integer id;
    private Integer productId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Timestamp createdAt;
}