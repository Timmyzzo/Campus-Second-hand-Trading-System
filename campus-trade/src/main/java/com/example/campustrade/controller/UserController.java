// src/main/java/com/example/campustrade/controller/UserController.java
package com.example.campustrade.controller;

import com.example.campustrade.entity.User;
import com.example.campustrade.service.UserService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Data
@RestController // 告诉Spring这是个控制器，并且返回的是JSON数据
@RequestMapping("/api/users") // 这个控制器下所有接口的URL前缀都是 /api/users
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return ResponseEntity.ok("注册成功！");
        } else {
            return ResponseEntity.badRequest().body("注册失败，用户名可能已存在。");
        }
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.login(username, password);

        if (user != null) {
            // 登录成功，为了安全，不返回密码
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }
}