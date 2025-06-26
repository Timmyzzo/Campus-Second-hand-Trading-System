package com.example.campustrade.service;

import com.example.campustrade.entity.User;
import com.example.campustrade.mapper.UserMapper;
import com.example.campustrade.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service //声明这是一个服务类，负责业务逻辑处理
public class UserService {

    @Autowired//自动注入 UserMapper 的实例,让他操作数据库
    private UserMapper userMapper;

    public boolean register(User user) {//检查用户名是否存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return false;
        }
        String encryptedPassword = EncryptionUtil.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        userMapper.insert(user);//再次调用Mapper的方法，将最终的用户数据写入数据库。
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
                // 如果密码匹配，登录成功，返回完整的User对象
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