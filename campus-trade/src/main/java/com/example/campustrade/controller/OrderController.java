package com.example.campustrade.controller;

import com.example.campustrade.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(@RequestBody Map<String, Integer> payload) {
        // ... 此方法保持不变 ...
        // 为了简洁省略，请保留你原来的代码
        Integer productId = payload.get("productId");
        Integer buyerId = payload.get("buyerId");
        logger.info("--- 接收到购买请求, 商品ID: {}, 购买者ID: {} ---", productId, buyerId);
        try {
            orderService.purchaseProduct(productId, buyerId);
            return ResponseEntity.ok("购买成功！");
        } catch (Exception e) {
            logger.error("--- 购买处理失败: {} ---", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 【最终修正版】
     * 这个Controller方法本身不需要改变，因为它本来就是接收一个 buyerId 和一个包含其他参数的 Map
     */
    @GetMapping("/my")
    public ResponseEntity<List<Map<String, Object>>> getMyOrders(
            @RequestParam Integer buyerId,
            @RequestParam Map<String, Object> params // 前端传来的其他参数会自动封装到这个Map里
    ) {
        List<Map<String, Object>> orders = orderService.getMyOrders(buyerId, params);
        return ResponseEntity.ok(orders);
    }
}