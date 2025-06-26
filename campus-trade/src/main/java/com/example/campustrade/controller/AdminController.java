package com.example.campustrade.controller;

import com.example.campustrade.entity.User;
import com.example.campustrade.service.AdminService;
import com.example.campustrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List; // <<< 新增的导入语句
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;

    @PostMapping("/products/takedown/{productId}")
    public ResponseEntity<String> takeDownProduct(@PathVariable Integer productId) {
        boolean success = adminService.takeDownProduct(productId);
        if (success) {
            return ResponseEntity.ok("商品下架成功！");
        }
        return ResponseEntity.badRequest().body("操作失败，商品可能不存在。");
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) {
        try {
            // 先删除关联的留言
            jdbcTemplate.update("DELETE FROM messages WHERE product_id = ?", id);
            // 再删除关联的订单
            jdbcTemplate.update("DELETE FROM orders WHERE product_id = ?", id);
            // 最后删除商品
            boolean success = adminService.deleteProduct(id);
            if(success) {
                return ResponseEntity.ok("商品删除成功");
            }
            return ResponseEntity.badRequest().body("商品删除失败，可能商品不存在。");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("删除失败: " + e.getMessage());
        }
    }


    //用一个Map接收所有查询条件
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@RequestParam Map<String, Object> params) {
        // params 这个 Map 会自动接收所有URL查询参数，如 keyword, category, minPrice 等

        List<Map<String, Object>> productsSummary = adminService.getProductsSummary(params);
        List<Map<String, Object>> salesSummary = adminService.getSalesSummary(params);

        Map<String, Object> result = new HashMap<>();
        result.put("productsSummary", productsSummary);
        result.put("salesSummary", salesSummary);

        return ResponseEntity.ok(result);
    }

    //required = false表示这个查询参数是可选的。如果前端不传username，这个参数就是null，而不会报错。
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String studentId
    ) {
        List<User> users = userService.getAllUsers(username, studentId);
        return ResponseEntity.ok(users);
    }


}