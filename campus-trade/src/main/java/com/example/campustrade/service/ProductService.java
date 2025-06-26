package com.example.campustrade.service;

import com.example.campustrade.entity.Product;
import com.example.campustrade.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    //发布新商品
    public boolean addProduct(Product product) {
        return productMapper.insert(product) > 0;
    }

    //获取所有在售商品 (用于主页)
    public List<Product> getAllOnSaleProducts() {
        return productMapper.findAllOnSale();
    }

    //获取我发布的商品 (用于个人中心，支持复杂查询)
    public List<Map<String, Object>> getMyProducts(Integer sellerId, Map<String, Object> params) {
        params.put("sellerId", sellerId);
        return productMapper.findBySellerIdWithConditions(params);
    }
}