package com.example.campustrade.service;

import com.example.campustrade.entity.User;
import com.example.campustrade.mapper.UserMapper;
import com.example.campustrade.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List; // <<< 新增的导入语句

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return false;
        }
        String encryptedPassword = EncryptionUtil.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        userMapper.insert(user);
        return true;
    }

    public User login(String username, String password) {
        // 管理员特殊登录通道
        if ("admin".equals(username) && "123456".equals(password)) {
            User adminUser = new User();
            adminUser.setId(0); // 管理员特殊ID
            adminUser.setUsername("admin");
            return adminUser;
        }

        User user = userMapper.findByUsername(username);
        if (user != null) {
            if (EncryptionUtil.encrypt(password).equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    /**
     * 新增：获取所有用户（带查询条件）
     * @param username 用户名关键词
     * @param studentId 学号
     * @return 用户列表
     */
    public List<User> getAllUsers(String username, String studentId) {
        return userMapper.findAll(username, studentId);
    }
}