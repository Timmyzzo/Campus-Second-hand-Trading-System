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
    //private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private ProductMapper productMapper;

    public boolean takeDownProduct(Integer productId) {
        return productMapper.updateStatusToTakenDown(productId) > 0;
    }


    // 获取挂牌信息汇总
    public List<Map<String, Object>> getProductsSummary(Map<String, Object> params) {
        //logger.info("--- [AdminService] getProductsSummary 接收到的参数: {}", params);
        return productMapper.getProductsSummary(params);//调用转发
        //直接调用ProductMapper中调用存储过程的方法
    }

    //获取成交情况汇总
    public List<Map<String, Object>> getSalesSummary(Map<String, Object> params) {
        //logger.info("--- [AdminService] getSalesSummary 接收到的参数: {}", params);
        return productMapper.getSalesSummary(params);//调用sp_get_sales_summary存储过程
    }


    // 删除商品
    public boolean deleteProduct(Integer productId) {

        return productMapper.deleteById(productId) > 0;
    }
}