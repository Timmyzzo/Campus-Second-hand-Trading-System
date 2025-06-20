package com.example.campustrade.controller;

import com.example.campustrade.entity.User; // <<< 新增的导入语句
import com.example.campustrade.service.AdminService;
import com.example.campustrade.service.UserService; // <<< 新增的导入语句
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
    private UserService userService; // <<< 新增的注入

    @PostMapping("/products/takedown/{productId}")
    public ResponseEntity<String> takeDownProduct(@PathVariable Integer productId) {
        boolean success = adminService.takeDownProduct(productId);
        if (success) {
            return ResponseEntity.ok("商品下架成功！");
        }
        return ResponseEntity.badRequest().body("操作失败，商品可能不存在。");
    }

    /**
     * 新增：删除商品接口
     * @param id 商品ID
     * @return 成功或失败的响应
     */
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




    // 原来的方法签名是：
// public ResponseEntity<Map<String, Object>> getSummary(@RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime)

    // 用下面的新版本替换它：
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






    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String studentId
    ) {
        List<User> users = userService.getAllUsers(username, studentId);
        return ResponseEntity.ok(users);
    }


}