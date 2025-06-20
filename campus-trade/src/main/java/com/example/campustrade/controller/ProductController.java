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
import java.util.Map; // <<< 就是缺少了这一行导入语句

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/publish")
    public ResponseEntity<String> publishProduct(@RequestBody Product product) {
        boolean success = productService.addProduct(product);
        if (success) {
            return ResponseEntity.ok("商品发布成功！");
        }
        return ResponseEntity.badRequest().body("发布失败，请检查输入信息。");
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllOnSaleProducts() {
        List<Product> products = productService.getAllOnSaleProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productMapper.findById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 【最终版】获取我发布的商品列表（支持复杂查询）
     * @param sellerId 卖家ID
     * @param params 包含所有查询条件的Map
     * @return 商品列表
     */
    @GetMapping("/my")
    public ResponseEntity<List<Map<String, Object>>> getMyProducts(
            @RequestParam Integer sellerId,
            @RequestParam Map<String, Object> params
    ) {
        List<Map<String, Object>> products = productService.getMyProducts(sellerId, params);
        return ResponseEntity.ok(products);
    }
}