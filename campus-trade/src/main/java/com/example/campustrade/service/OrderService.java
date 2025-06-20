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
    private JdbcTemplate jdbcTemplate;

    public boolean purchaseProduct(Integer productId, Integer buyerId) {
        // ... 此方法保持不变 ...
        // 为了简洁省略，请保留你原来的代码
        logger.info("--- 准备执行购买操作，商品ID: {}, 购买者ID: {} ---", productId, buyerId);
        try {
            jdbcTemplate.update("CALL sp_purchase_product(?, ?)", productId, buyerId);
            logger.info("--- 存储过程 sp_purchase_product 执行成功 ---");
            return true;
        } catch (Exception e) {
            logger.error("--- 调用购买存储过程失败！原因: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 【最终修正版】
     * 在调用Mapper前，将 buyerId 也放入 params 这个Map中
     */
    public List<Map<String, Object>> getMyOrders(Integer buyerId, Map<String, Object> params) {
        // 将 buyerId 添加到查询参数 Map 中
        params.put("buyerId", buyerId);
        return orderMapper.findByConditions(params);
    }
}