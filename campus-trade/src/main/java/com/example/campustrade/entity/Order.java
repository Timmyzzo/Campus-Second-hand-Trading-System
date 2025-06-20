package com.example.campustrade.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Order {
    private Integer id;
    private Integer productId;
    private Integer buyerId;
    private Integer sellerId;
    private BigDecimal orderPrice;
    private Timestamp createdAt;
}