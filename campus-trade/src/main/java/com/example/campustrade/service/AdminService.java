package com.example.campustrade.service;

import com.example.campustrade.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.campustrade.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private ProductMapper productMapper;

    public boolean takeDownProduct(Integer productId) {
        return productMapper.updateStatusToTakenDown(productId) > 0;
    }

    // 原来的方法是：
// public List<Map<String, Object>> getProductsSummary(String startTime, String endTime)

    // 修改为：
    public List<Map<String, Object>> getProductsSummary(Map<String, Object> params) {
        logger.info("--- [AdminService] getProductsSummary 接收到的参数: {}", params);
        return productMapper.getProductsSummary(params);
    }

    public List<Map<String, Object>> getSalesSummary(Map<String, Object> params) {
        logger.info("--- [AdminService] getSalesSummary 接收到的参数: {}", params);
        return productMapper.getSalesSummary(params);
    }


    public boolean deleteProduct(Integer productId) {
        // 在真实项目中，删除前可能需要先删除关联的留言和订单，但这里为了简化，我们直接删除
        return productMapper.deleteById(productId) > 0;
    }
}