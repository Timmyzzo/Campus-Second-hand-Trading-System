// src/main/java/com/example/campustrade/controller/UserController.java
//用户控制器，处理用户注册和登录请求
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
@RestController // 控制器，返回JSON数据
@RequestMapping("/api/users") // 定义了这个控制器管辖的URL前缀


public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register") //指明这个方法只处理 HTTP POST 类型的请求
    public ResponseEntity<String> register(@RequestBody User user) {
        //接收前端传来的JSON数据，spring自动转成一个User对象:new User()
        boolean success = userService.register(user);
        if (success) {
            return ResponseEntity.ok("注册成功！"); //返回http响应
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