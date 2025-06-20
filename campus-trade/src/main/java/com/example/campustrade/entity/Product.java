package com.example.campustrade.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Product {
    private Integer id;
    private Integer sellerId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private String status;
    private Timestamp createdAt;
}