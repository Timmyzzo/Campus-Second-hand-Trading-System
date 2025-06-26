package com.example.campustrade.controller;

import com.example.campustrade.entity.Product;
import com.example.campustrade.mapper.ProductMapper;
import com.example.campustrade.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    //private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/publish")//发布商品
    public ResponseEntity<String> publishProduct(@RequestBody Product product) {
        // 调用Service，根据返回结果响应
        boolean success = productService.addProduct(product);
        if (success) {
            return ResponseEntity.ok("商品发布成功！");
        }
        return ResponseEntity.badRequest().body("发布失败，请检查输入信息。");
    }

    @GetMapping// 映射到 GET /api/products
    public ResponseEntity<List<Product>> getAllOnSaleProducts() {
        List<Product> products = productService.getAllOnSaleProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")//影射到对应商品de ID
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productMapper.findById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/my")
    public ResponseEntity<List<Map<String, Object>>> getMyProducts(
            @RequestParam Integer sellerId,//从查询参数中获取sellerId
            @RequestParam Map<String, Object> params//获取所有查询参数到Map中
    ) {
        List<Map<String, Object>> products = productService.getMyProducts(sellerId, params);
        return ResponseEntity.ok(products);
    }
}