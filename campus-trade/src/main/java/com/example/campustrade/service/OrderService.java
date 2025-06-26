package com.example.campustrade.service;

import com.example.campustrade.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;//用来调用一个没有返回结果集的存储过程（只做事，不查询）

    public boolean purchaseProduct(Integer productId, Integer buyerId) {
        try {
            jdbcTemplate.update("CALL sp_purchase_product(?, ?)", productId, buyerId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //获取我的订单 (支持复杂查询)
    public List<Map<String, Object>> getMyOrders(Integer buyerId, Map<String, Object> params) {
        // 将 buyerId 添加到查询参数 Map 中
        params.put("buyerId", buyerId);
        return orderMapper.findByConditions(params);
    }
}