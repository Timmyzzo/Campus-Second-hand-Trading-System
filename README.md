我现在正在做我的数据库课程设计，在AI的帮助下已经在各种方式下做完了大部分的功能，但是无奈AI有上下文的token限制，所以我在此重开一个对话，排除一切错误的代码和讲解步骤等，我将先告诉你我的题目要求，再告诉你我项目的主要部分代码（只有核心的，如果你还需要我没提到的才能确认我程序的功能的话也可以告诉我），请你仔细思考，综合分析以下全部内容。目前这些代码是可以正常运行，大概基本可以完成我的要求，但是因为我还想完善添加一些新功能和准备这个课程设计的答辩，所以请你先进行分析，随后我再向你提问。

要求：

数据库课程设计选题：校园二手交易系统的设计与实现

1.概述：参考闲鱼，转转等二手交易网站。

2.系统基本功能：

2.1 基本：
1)进入网站：http://localhost:5178/ （不一定是要5178，这里只是主页面的意思，后同）后，需要进行注册或登录才可以使用
2)注册需要输入自己的用户名和密码，性别，学号（学号只能后台管理员和此用户本人看到）
3)用户登录成功后进入http://localhost:5178/home/products 也就是商品展示界面，可以看到所有在售的商品
4)所有用户都可以发布商品，进入http://localhost:5178/home/publish  ，输入商品名称，分类，价格，详细描述，图片链接后即可发布
5)所有用户都可以进入个人中心 http://localhost:5178/home/profile ，可以分别查看 我发布的和我买到的，也就是说卖家可以查询自己上架的所有货物，买家可以查询自己的所有订单信息，可以按时间段或商品名称关键词或商品分类或价格进行查询汇总（可多个条件同时选择复杂查询），以报表形式给出
6)用户可以进入消息中心 http://localhost:5178/home/messages，查看消息
7)在没有返回键的界面，可以点击始终处于左上角的“校园二手交易系统”几个字，即可转到商品主界面：http://localhost:5178/home/products
8)在商品主界面点击感兴趣的商品就可以进入商品详情
9)既然是校园二手交易平台，买卖双方面交，这里的购买不用考虑余额，只是作为价格的显示，所以不存在余额不足购买失败的情况
10)所有前端页面的文字必须和白色背景是高对比的颜色，便于用户看清

2.2 用户作为买家时：
1)点开你想购买的商品，进入对应商品的详情，可以点击立即购买进行购买
2)点开你想购买的商品，进入对应商品的详情，可以点击留言咨询与卖家进行留言咨询


2.3 用户作为卖家时：
1)卖家不可以购买自己的商品
2)卖家不可以和自己留言咨询
3)卖家进入消息中心可以看到买家给自己的留言，并且在其中可以与买家进行交流

2.4 管理员：
1)输入固定的账号:admin 和密码:123456 ，即可进入管理员界面
2)管理员可以进行后台信息维护：比如违规的物品，管理员可以强制删除这条记录（无需让卖家知道是被强制下架，直接从数据库中移除这条记录）
3)后台信息汇总：作为管理员，可以汇总统计一段时间内按物品种类等物品挂牌信息、成交情况，这两个分别都可以按时间段或商品名称关键词或商品分类或价格进行查询汇总（可多个条件同时选择复杂查询），以报表形式给出
4)可以查看所有的用户，并且可以根据姓名或学号进行查询，以报表形式给出


3.数据库完整性设计：
3.1：设计者应认真分析和思考各个表之间的关系，合理设计和实施数据完整性原则。给每个表实施主键及外键约束。
3.2：设定缺省约束。
3.3：设置非空约束。
3.4：实施CHECK约束。
3.5：储存在数据库中的密码应该才用加密，这里不是实际生产环境所以使用最简单的凯撒加密或者其他任意容易实现的加密方法即可，越简单越好。

4.数据库对象设计：
4.1：为充分发挥数据库的效能，保证数据库的安全性，提高数据库管理系统的执行效率，可以考虑使用视图、存储过程及表的触发器来实现某些功能。
4.2：要求至少设计一个存储过程、一个视图，并在系统中调用；一个触发器，并发挥作用。其它对象用户可按需求自行设计。

5.开发情况
5.1：开发工具：IntelliJ IDEA 2024.1.7+JDK21+my sql workbench
5.2：技术栈：前后端分离的web系统，采用springboot3+vue3的架构，具体为前端使用Vue3+Element-Plus，后端使用Springboot3+Mybatis技术

后端架构：

campus-trade/
└── src/
    └── main/
        ├── java/
        │   └── com/example/campustrade/
        │       ├── CampusTradeApplication.java  -- (1) 项目启动入口
        │       ├── controller/                  -- (2) 控制层 (Controller)
        │       │   ├── AdminController.java
        │       │   ├── MessageController.java
        │       │   ├── OrderController.java
        │       │   ├── ProductController.java
        │       │   └── UserController.java
        │       ├── service/                     -- (3) 业务逻辑层 (Service)
        │       │   ├── AdminService.java
        │       │   ├── MessageService.java
        │       │   ├── OrderService.java
        │       │   ├── ProductService.java
        │       │   └── UserService.java
        │       ├── mapper/                      -- (4) 数据访问层 (Mapper/DAO)
        │       │   ├── MessageMapper.java
        │       │   ├── OrderMapper.java
        │       │   ├── ProductMapper.java
        │       │   └── UserMapper.java
        │       ├── entity/                      -- (5) 实体类层 (Entity/Model)
        │       │   ├── Message.java
        │       │   ├── Order.java
        │       │   ├── Product.java
        │       │   └── User.java
        │       └── util/                        -- (6) 工具类层 (Utility)
        │           └── EncryptionUtil.java
        └── resources/
            ├── static/
            ├── templates/
            └── application.properties           -- (7) 核心配置文件

CampusTradeApplication.java：

```java
// 这是正确的、适用于你当前项目结构的代码
package com.example.campustrade; // 注意：这里的包名是根据你的截图来的

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 之前的路径是 "com.cenzhihao.campus_trade.mapper"，是错误的
// 正确的路径应该是你项目里 mapper 包的完整路径
@MapperScan("com.example.campustrade.mapper")
public class CampusTradeApplication {

    public static void main(String[] args) {
       SpringApplication.run(CampusTradeApplication.class, args);
    }

}
```



AdminController.java：

```java
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
```



MessageController:

```java
package com.example.campustrade.controller;

import com.example.campustrade.entity.Message;
import com.example.campustrade.mapper.MessageMapper;
import com.example.campustrade.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper messageMapper;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        // sendMessage 逻辑保持不变，但我们确保其健壮性
        boolean success = messageService.sendMessage(message);
        if (success) {
            return ResponseEntity.ok("留言成功！");
        }
        return ResponseEntity.badRequest().body("留言失败！");
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(
            @RequestParam Integer productId,
            @RequestParam Integer userId1,
            @RequestParam Integer userId2) {
        List<Message> messages = messageService.getMessages(productId, userId1, userId2);
        return ResponseEntity.ok(messages);
    }

    /**
     * 新增：获取我的所有对话列表
     * @param userId 当前登录用户的ID
     * @return 对话列表
     */
    @GetMapping("/my-conversations")
    public ResponseEntity<List<Map<String, Object>>> getMyConversations(@RequestParam Integer userId) {
        List<Map<String, Object>> conversations = messageMapper.findMyConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}
```





OrderController:

```java
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
```







ProductController：

```java
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
```



UserController:

```java
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
```



Message:

```java
package com.example.campustrade.entity;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Message {
    private Integer id;
    private Integer productId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Timestamp createdAt;
}
```



Order:

```java
package com.example.campustrade.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Order {
    private Integer id;
    private Integer productId;
    private Integer buyerId;
    private Integer sellerId;
    private BigDecimal orderPrice;
    private Timestamp createdAt;
}
```



Product:

```java
package com.example.campustrade.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Product {
    private Integer id;
    private Integer sellerId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private String status;
    private Timestamp createdAt;
}
```





User:

```java
package com.example.campustrade.entity;

import lombok.Data; // 引入Lombok
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data // Lombok注解, 自动生成getter, setter, toString等方法
public class User {
    private Integer id;
    private String username;
    private String password;
    private String gender;
    private String studentId; // 驼峰命名法
    private BigDecimal balance;
    private Timestamp createdAt;

}
```



MessageMapper：

```java
package com.example.campustrade.mapper;

import com.example.campustrade.entity.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO messages(product_id, sender_id, receiver_id, content) " +
            "VALUES(#{productId}, #{senderId}, #{receiverId}, #{content})")
    int insert(Message message);

    @Select("SELECT * FROM messages WHERE product_id = #{productId} " +
            "AND ((sender_id = #{userId1} AND receiver_id = #{userId2}) OR (sender_id = #{userId2} AND receiver_id = #{userId1})) " +
            "ORDER BY created_at ASC")
    List<Message> findMessagesBetweenUsers(Integer productId, Integer userId1, Integer userId2);

    /**
     * 新增：获取与我相关的所有对话的最新一条消息
     * 这是一个复杂查询，用于构建消息中心列表
     */
    @Select("SELECT m1.*, p.name as productName, " +
            "       IF(m1.sender_id = #{userId}, r.username, s.username) as otherPartyUsername " +
            "FROM messages m1 " +
            "INNER JOIN ( " +
            "    SELECT " +
            "        product_id, " +
            "        LEAST(sender_id, receiver_id) as user_a, " +
            "        GREATEST(sender_id, receiver_id) as user_b, " +
            "        MAX(id) as max_id " +
            "    FROM messages " +
            "    WHERE sender_id = #{userId} OR receiver_id = #{userId} " +
            "    GROUP BY product_id, user_a, user_b " +
            ") m2 ON m1.id = m2.max_id " +
            "LEFT JOIN products p ON m1.product_id = p.id " +
            "LEFT JOIN users s ON m1.sender_id = s.id " +
            "LEFT JOIN users r ON m1.receiver_id = r.id " +
            "ORDER BY m1.created_at DESC")
    List<Map<String, Object>> findMyConversations(Integer userId);
}
```



OrderMapper:

```java
package com.example.campustrade.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider; // 注意：我们换用 SelectProvider
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 【数据库视图版】
     * 使用 SelectProvider 动态构建对 v_orders_details 视图的查询
     */
    @SelectProvider(type = OrderSqlBuilder.class, method = "buildFindByConditions")
    List<Map<String, Object>> findByConditions(Map<String, Object> params);
}
```



OrderSqlBuilder:

```java
package com.example.campustrade.mapper;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class OrderSqlBuilder {

    public String buildFindByConditions(Map<String, Object> params) {
        return new SQL() {{
            // 查询我们刚刚创建的视图
            SELECT("*");
            FROM("v_orders_details");

            // 动态添加查询条件
            if (params.get("buyerId") != null) {
                WHERE("buyer_id = #{buyerId}");
            }
            if (params.get("startTime") != null && !params.get("startTime").toString().isEmpty()) {
                WHERE("created_at >= #{startTime}");
            }
            if (params.get("endTime") != null && !params.get("endTime").toString().isEmpty()) {
                WHERE("created_at <= #{endTime}");
            }
            if (params.get("category") != null && !params.get("category").toString().isEmpty()) {
                WHERE("product_category = #{category}");
            }
            if (params.get("keyword") != null && !params.get("keyword").toString().isEmpty()) {
                WHERE("product_name LIKE CONCAT('%', #{keyword}, '%')");
            }
            if (params.get("minPrice") != null) {
                WHERE("order_price >= #{minPrice}");
            }
            if (params.get("maxPrice") != null) {
                WHERE("order_price <= #{maxPrice}");
            }

            ORDER_BY("created_at DESC");
        }}.toString();
    }
}
```



ProductMapper:

```java
package com.example.campustrade.mapper;

import com.example.campustrade.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {

    // --- 简单查询方法 ---
    @Select("SELECT * FROM products WHERE id = #{id}")
    Product findById(Integer id);

    @Insert("INSERT INTO products(seller_id, name, description, category, price, image_url) " +
            "VALUES(#{sellerId}, #{name}, #{description}, #{category}, #{price}, #{imageUrl})")
    int insert(Product product);

    @Select("SELECT id, seller_id as sellerId, name, description, category, price, image_url as imageUrl, status, created_at as createdAt FROM products WHERE status = '在售' ORDER BY created_at DESC")
    List<Product> findAllOnSale();

    @Delete("DELETE FROM products WHERE id = #{id}")
    int deleteById(Integer id);

    @Update("UPDATE products SET status = '已下架' WHERE id = #{productId}")
    int updateStatusToTakenDown(Integer productId);

    // --- 复杂查询方法，现在全部改为调用存储过程 ---

    @Select("CALL sp_get_my_products(#{sellerId}, #{keyword}, #{category}, #{minPrice}, #{maxPrice}, #{startTime}, #{endTime})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String, Object>> findBySellerIdWithConditions(Map<String, Object> params);

    @Select("CALL sp_get_products_summary(#{keyword}, #{category}, #{minPrice}, #{maxPrice}, #{startTime}, #{endTime})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String, Object>> getProductsSummary(Map<String, Object> params);

    @Select("CALL sp_get_sales_summary(#{keyword}, #{category}, #{minPrice}, #{maxPrice}, #{startTime}, #{endTime})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String, Object>> getSalesSummary(Map<String, Object> params);
}
```



UserMapper:

```java
package com.example.campustrade.mapper;

import com.example.campustrade.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

@Mapper
public interface UserMapper {

    // 简单的查询和插入，继续使用注解
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users(username, password, gender, student_id) " +
            "VALUES(#{username}, #{password}, #{gender}, #{studentId})")
    void insert(User user);

    /**
     * 【最终解决方案】
     * 调用数据库存储过程 sp_get_users 来执行复杂查询。
     * @param username 用户名关键词
     * @param studentId 学号
     * @return 用户列表
     */
    @Select("CALL sp_get_users(#{username}, #{studentId})")
    @Options(statementType = StatementType.CALLABLE)
    List<User> findAll(String username, String studentId);
}
```



AdminService:

```java
package com.example.campustrade.service;

import com.example.campustrade.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.campustrade.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private ProductMapper productMapper;

    public boolean takeDownProduct(Integer productId) {
        return productMapper.updateStatusToTakenDown(productId) > 0;
    }

    // 原来的方法是：
// public List<Map<String, Object>> getProductsSummary(String startTime, String endTime)

    // 修改为：
    public List<Map<String, Object>> getProductsSummary(Map<String, Object> params) {
        logger.info("--- [AdminService] getProductsSummary 接收到的参数: {}", params);
        return productMapper.getProductsSummary(params);
    }

    public List<Map<String, Object>> getSalesSummary(Map<String, Object> params) {
        logger.info("--- [AdminService] getSalesSummary 接收到的参数: {}", params);
        return productMapper.getSalesSummary(params);
    }


    public boolean deleteProduct(Integer productId) {
        // 在真实项目中，删除前可能需要先删除关联的留言和订单，但这里为了简化，我们直接删除
        return productMapper.deleteById(productId) > 0;
    }
}
```



MessageService:

```java
// src/main/java/com/example/campustrade/service/MessageService.java
package com.example.campustrade.service;

import com.example.campustrade.entity.Message;
import com.example.campustrade.entity.Product;
import com.example.campustrade.mapper.MessageMapper;
import com.example.campustrade.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ProductMapper productMapper; // 引入ProductMapper来查询商品信息

    // 发送留言
    // 在 MessageService.java 中
    public boolean sendMessage(Message message) {
        Product product = productMapper.findById(message.getProductId());
        if (product == null) {
            return false;
        }

        // 新逻辑：如果前端没有指定接收者，我们才默认接收者是卖家。
        // 这样就允许卖家回复时，前端可以明确指定接收者是哪个买家。
        if (message.getReceiverId() == null) {
            // 如果发送者不是卖家，那他一定是买家，接收者就应该是卖家
            if (!message.getSenderId().equals(product.getSellerId())) {
                message.setReceiverId(product.getSellerId());
            } else {
                // 卖家不能在没有指定接收者的情况下发送消息
                return false;
            }
        }

        // 不能给自己发消息
        if (message.getSenderId().equals(message.getReceiverId())) {
            return false;
        }

        return messageMapper.insert(message) > 0;
    }

    // 获取两个用户在某个商品下的对话
    public List<Message> getMessages(Integer productId, Integer userId1, Integer userId2) {
        return messageMapper.findMessagesBetweenUsers(productId, userId1, userId2);
    }
}
```



OrderService:

```java
package com.example.campustrade.service;

import com.example.campustrade.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean purchaseProduct(Integer productId, Integer buyerId) {
        // ... 此方法保持不变 ...
        // 为了简洁省略，请保留你原来的代码
        logger.info("--- 准备执行购买操作，商品ID: {}, 购买者ID: {} ---", productId, buyerId);
        try {
            jdbcTemplate.update("CALL sp_purchase_product(?, ?)", productId, buyerId);
            logger.info("--- 存储过程 sp_purchase_product 执行成功 ---");
            return true;
        } catch (Exception e) {
            logger.error("--- 调用购买存储过程失败！原因: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 【最终修正版】
     * 在调用Mapper前，将 buyerId 也放入 params 这个Map中
     */
    public List<Map<String, Object>> getMyOrders(Integer buyerId, Map<String, Object> params) {
        // 将 buyerId 添加到查询参数 Map 中
        params.put("buyerId", buyerId);
        return orderMapper.findByConditions(params);
    }
}
```





ProductService:

```java
package com.example.campustrade.service;

import com.example.campustrade.entity.Product;
import com.example.campustrade.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map; // 确保导入 Map

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 发布新商品
     */
    public boolean addProduct(Product product) {
        return productMapper.insert(product) > 0;
    }

    /**
     * 获取所有在售商品 (用于主页)
     */
    public List<Product> getAllOnSaleProducts() {
        return productMapper.findAllOnSale();
    }

    /**
     * 【最终版】获取我发布的商品 (用于个人中心，支持复杂查询)
     * @param sellerId 卖家ID
     * @param params 包含查询条件的Map
     * @return 商品列表
     */
    public List<Map<String, Object>> getMyProducts(Integer sellerId, Map<String, Object> params) {
        params.put("sellerId", sellerId);
        return productMapper.findBySellerIdWithConditions(params);
    }
}
```



UserService:

```java
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
```



EncryptionUtil：

```java
package com.example.campustrade.util;

public class EncryptionUtil {

    private static final int SHIFT = 3; // 移动3位

    // 加密
    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        StringBuilder encryptedText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            encryptedText.append((char) (c + SHIFT));
        }
        return encryptedText.toString();
    }

    // 解密 (虽然本次用不到，但写上总没错)
    public static String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }
        StringBuilder plainText = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            plainText.append((char) (c - SHIFT));
        }
        return plainText.toString();
    }
}
```



application.properties:

```properties
# 服务器端口号，可以随便改，别冲突就行
server.port=8080

# 数据库连接配置
spring.datasource.url=jdbc:mysql://localhost:3306/campus_trade_db?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# MyBatis 配置
# mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.map-underscore-to-camel-case=true

# 开启MyBatis对指定Mapper接口的详细日志输出
logging.level.com.example.campustrade.mapper.ProductMapper=TRACE
```



数据库文件:

```sql
-- =================================================================
-- 校园二手交易系统 - 最终版数据库脚本
-- 日期: 2025-06-20
-- 描述: 包含所有表、约束、视图、存储过程和触发器。
-- =================================================================

-- 步骤1：创建并使用数据库
-- -----------------------------------------------------------------
DROP DATABASE IF EXISTS `campus_trade_db`;
CREATE DATABASE `campus_trade_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
USE `campus_trade_db`;

-- 步骤2：创建表结构
-- -----------------------------------------------------------------

-- (1) 用户表 (users)
CREATE TABLE `users` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID, 主键',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名, 非空, 唯一',
  `password` VARCHAR(255) NOT NULL COMMENT '密码, 非空',
  `gender` VARCHAR(10) NOT NULL COMMENT '性别, 非空',
  `student_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '学号, 非空, 唯一',
  `balance` DECIMAL(10, 2) NOT NULL DEFAULT 10000.00 COMMENT '余额, 非空, 默认10000',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  CONSTRAINT `chk_gender` CHECK (`gender` IN ('男', '女'))
) COMMENT='用户信息表';

-- (2) 商品表 (products)
CREATE TABLE `products` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID, 主键',
  `seller_id` INT NOT NULL COMMENT '卖家ID, 外键',
  `name` VARCHAR(100) NOT NULL COMMENT '商品名称, 非空',
  `description` TEXT COMMENT '商品描述',
  `category` VARCHAR(50) NOT NULL COMMENT '物品种类, 非空',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '价格, 非空',
  `image_url` VARCHAR(255) COMMENT '图片链接',
  `status` VARCHAR(20) NOT NULL DEFAULT '在售' COMMENT '商品状态: 在售, 已售, 已下架',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上架时间',
  CONSTRAINT `fk_products_seller` FOREIGN KEY (`seller_id`) REFERENCES `users`(`id`),
  CONSTRAINT `chk_price` CHECK (`price` > 0),
  CONSTRAINT `chk_status` CHECK (`status` IN ('在售', '已售', '已下架'))
) COMMENT='商品信息表';

-- (3) 留言表 (messages)
CREATE TABLE `messages` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '留言ID, 主键',
  `product_id` INT NOT NULL COMMENT '关联的商品ID',
  `sender_id` INT NOT NULL COMMENT '发送者ID',
  `receiver_id` INT NOT NULL COMMENT '接收者ID',
  `content` TEXT NOT NULL COMMENT '留言内容, 非空',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
  CONSTRAINT `fk_messages_product` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`),
  CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users`(`id`),
  CONSTRAINT `fk_messages_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users`(`id`)
) COMMENT='用户留言表';

-- (4) 订单表 (orders)
CREATE TABLE `orders` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID, 主键',
  `product_id` INT NOT NULL UNIQUE COMMENT '交易的商品ID, 唯一',
  `buyer_id` INT NOT NULL COMMENT '买家ID',
  `seller_id` INT NOT NULL COMMENT '卖家ID',
  `order_price` DECIMAL(10, 2) NOT NULL COMMENT '订单成交价格',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  CONSTRAINT `fk_orders_product` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`),
  CONSTRAINT `fk_orders_buyer` FOREIGN KEY (`buyer_id`) REFERENCES `users`(`id`),
  CONSTRAINT `fk_orders_seller` FOREIGN KEY (`seller_id`) REFERENCES `users`(`id`)
) COMMENT='订单信息表';


-- 步骤3：创建数据库对象 (视图, 存储过程, 触发器)
-- -----------------------------------------------------------------

-- (1) 视图：v_orders_details (简化订单详情查询)
CREATE OR REPLACE VIEW `v_orders_details` AS
SELECT 
    o.id AS order_id,
    o.order_price,
    o.created_at,
    o.buyer_id,
    o.seller_id,
    p.id AS product_id,
    p.name AS product_name,
    p.category AS product_category
FROM 
    orders o
JOIN 
    products p ON o.product_id = p.id;

-- (2) 触发器：trg_prevent_self_purchase (防止用户购买自己的商品)
DELIMITER $$
CREATE TRIGGER `trg_prevent_self_purchase`
BEFORE INSERT ON `orders`
FOR EACH ROW
BEGIN
    IF NEW.buyer_id = NEW.seller_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '用户不能购买自己发布的商品';
    END IF;
END$$
DELIMITER ;

-- (3) 存储过程：sp_purchase_product (处理购买逻辑，无余额检查版)
DELIMITER $$
CREATE PROCEDURE `sp_purchase_product`(IN p_product_id INT, IN p_buyer_id INT)
BEGIN
    DECLARE v_seller_id INT;
    DECLARE v_price DECIMAL(10, 2);
    DECLARE v_product_status VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    START TRANSACTION;

    SELECT seller_id, price, status INTO v_seller_id, v_price, v_product_status FROM products WHERE id = p_product_id FOR UPDATE;
    IF v_product_status != '在售' THEN ROLLBACK; SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '商品不是在售状态'; END IF;
    
    UPDATE products SET status = '已售' WHERE id = p_product_id;
    INSERT INTO orders (product_id, buyer_id, seller_id, order_price) VALUES (p_product_id, p_buyer_id, v_seller_id, v_price);

    COMMIT;
END$$
DELIMITER ;

-- (4) 存储过程：查询“我发布的”商品（带复杂条件）
DELIMITER $$
CREATE PROCEDURE `sp_get_my_products`(
    IN p_sellerId INT,
    IN p_keyword VARCHAR(100),
    IN p_category VARCHAR(50),
    IN p_minPrice DECIMAL(10,2),
    IN p_maxPrice DECIMAL(10,2),
    IN p_startTime DATETIME,
    IN p_endTime DATETIME
)
BEGIN
    SELECT 
        id, seller_id as sellerId, name, description, category, price, image_url as imageUrl, status, created_at as createdAt
    FROM products
    WHERE 
        seller_id = p_sellerId
        AND (p_keyword IS NULL OR name LIKE CONCAT('%', p_keyword, '%'))
        AND (p_category IS NULL OR category = p_category)
        AND (p_minPrice IS NULL OR price >= p_minPrice)
        AND (p_maxPrice IS NULL OR price <= p_maxPrice)
        AND (p_startTime IS NULL OR created_at >= p_startTime)
        AND (p_endTime IS NULL OR created_at <= p_endTime)
    ORDER BY created_at DESC;
END$$
DELIMITER ;

-- (5) 存储过程：查询“挂牌信息汇总”（带复杂条件）
DELIMITER $$
CREATE PROCEDURE `sp_get_products_summary`(
    IN p_keyword VARCHAR(100),
    IN p_category VARCHAR(50),
    IN p_minPrice DECIMAL(10,2),
    IN p_maxPrice DECIMAL(10,2),
    IN p_startTime DATETIME,
    IN p_endTime DATETIME
)
BEGIN
    SELECT 
        category, 
        COUNT(*) as total_count,
        SUM(CASE WHEN status = '在售' THEN 1 ELSE 0 END) as on_sale_count,
        SUM(CASE WHEN status = '已售' THEN 1 ELSE 0 END) as sold_count
    FROM products p
    WHERE
        (p_keyword IS NULL OR p.name LIKE CONCAT('%', p_keyword, '%'))
        AND (p_category IS NULL OR p.category = p_category)
        AND (p_minPrice IS NULL OR p.price >= p_minPrice)
        AND (p_maxPrice IS NULL OR p.price <= p_maxPrice)
        AND (p_startTime IS NULL OR p.created_at >= p_startTime)
        AND (p_endTime IS NULL OR p.created_at <= p_endTime)
    GROUP BY p.category;
END$$
DELIMITER ;

-- (6) 存储过程：查询“成交情况汇总”（带复杂条件）
DELIMITER $$
CREATE PROCEDURE `sp_get_sales_summary`(
    IN p_keyword VARCHAR(100),
    IN p_category VARCHAR(50),
    IN p_minPrice DECIMAL(10,2),
    IN p_maxPrice DECIMAL(10,2),
    IN p_startTime DATETIME,
    IN p_endTime DATETIME
)
BEGIN
    SELECT 
        p.category, 
        COUNT(o.id) as transaction_count, 
        SUM(o.order_price) as total_turnover
    FROM orders o 
    JOIN products p ON o.product_id = p.id
    WHERE
        (p_keyword IS NULL OR p.name LIKE CONCAT('%', p_keyword, '%'))
        AND (p_category IS NULL OR p.category = p_category)
        AND (p_minPrice IS NULL OR p.price >= p_minPrice)
        AND (p_maxPrice IS NULL OR p.price <= p_maxPrice)
        AND (p_startTime IS NULL OR o.created_at >= p_startTime)
        AND (p_endTime IS NULL OR o.created_at <= p_endTime)
    GROUP BY p.category;
END$$
DELIMITER ;

-- 脚本结束
-- -----------------------------------------------------------------
SELECT '数据库 campus_trade_db 创建和初始化完成！' AS '状态';






-- 使用你的数据库
USE campus_trade_db;

-- --------------------------------------------------------
-- 创建存储过程：查询用户（带条件）
-- --------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE `sp_get_users`(
    IN p_username VARCHAR(50),
    IN p_studentId VARCHAR(20)
)
BEGIN
    SELECT 
        id, 
        username, 
        gender, 
        student_id as studentId  -- 明确使用别名，确保Java端能正确映射
    FROM 
        users
    WHERE
        -- 如果传入的参数是NULL或空字符串，则忽略该条件
        (p_username IS NULL OR p_username = '' OR username LIKE CONCAT('%', p_username, '%'))
        AND 
        (p_studentId IS NULL OR p_studentId = '' OR student_id = p_studentId);
END$$
DELIMITER ;
```







前端架构：

campus-trade-frontend/
├── public/
├── src/
│   ├── api/                     -- (1) API请求模块
│   │   ├── admin.js
│   │   ├── message.js
│   │   ├── order.js
│   │   ├── product.js
│   │   └── user.js
│   ├── assets/                  -- (2) 静态资源 (CSS, 图片等)
│   ├── components/              -- (3) 可复用的小组件 (本项目简化，未使用)
│   ├── layouts/                 -- (4) 布局组件
│   │   └── Layout.vue
│   ├── router/                  -- (5) 路由配置
│   │   └── index.js
│   ├── views/                   -- (6) 视图/页面组件
│   │   ├── AdminView.vue
│   │   ├── DetailView.vue
│   │   ├── HomeView.vue
│   │   ├── LoginView.vue
│   │   ├── MessageCenterView.vue
│   │   ├── ProfileView.vue
│   │   ├── PublishView.vue
│   │   └── RegisterView.vue
│   ├── App.vue                  -- (7) 根组件
│   └── main.js                  -- (8) 项目入口文件
├── index.html
├── package.json                 -- (9) 项目依赖和脚本配置
└── vite.config.js               -- (10) Vite构建配置文件



admin.js:

```js
// src/api/admin.js (最终完整版)
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

// 获取汇总统计数据 (这个函数本身设计得很好，无需修改)
export const getSummaryService = (params) => {
  return request.get('/admin/summary', { params });
}

// 删除商品
export const deleteProductService = (id) => {
  return request.delete(`/admin/products/${id}`);
}

// 获取所有用户列表
export const getAllUsersService = (params) => {
  return request.get('/admin/users', { params });
}
```



message.js:

```js
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

/**
 * 获取某个商品下，两个用户之间的对话列表
 * @param {number} productId 商品ID
 * @param {number} userId1 用户1的ID
 * @param {number} userId2 用户2的ID
 */
export const getMessagesService = (productId, userId1, userId2) => {
  return request.get('/messages', { params: { productId, userId1, userId2 } });
}

/**
 * 发送一条新留言
 * @param {object} messageData 包含 productId, senderId, receiverId, content 的对象
 */
export const sendMessageService = (messageData) => {
  return request.post('/messages', messageData);
}

/**
 * 【新增】获取我的所有对话列表
 * @param {number} userId 当前登录用户的ID
 */
export const getMyConversationsService = (userId) => {
  return request.get('/messages/my-conversations', { params: { userId } });
}
```



order.js:

```js
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

/**
 * 购买商品接口
 * @param {object} payload - 包含 productId 和 buyerId 的对象
 */
export const purchaseService = (payload) => {
  return request.post('/orders/purchase', payload);
}

/**
 * 获取我的订单列表
 * @param {number} buyerId - 当前登录用户的ID
 */
export const getMyOrdersService = (buyerId, params) => {
  return request.get('/orders/my', { params: { buyerId, ...params } });
}
```





product.js:

```js
// src/api/product.js
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

// 获取所有在售商品
export const getProductsService = () => {
  return request.get('/products');
}

// 在 product.js 中新增
// 发布新商品
export const publishProductService = (productData) => {
  return request.post('/products/publish', productData);
}

// 在 product.js 中新增
// 根据ID获取商品详情
export const getProductByIdService = (id) => {
  // 这里的URL `/products/${id}` 会匹配到我们后端写的 @GetMapping("/{id}")
  return request.get(`/products/${id}`);
}

// 获取我发布的商品（支持复杂查询）
export const getMyProductsService = (sellerId, params) => {
  return request.get('/products/my', { params: { sellerId, ...params } });
}

```





user.js:

```js
// src/api/user.js
import axios from 'axios';

// 创建axios实例，配置基础路径
const request = axios.create({
  baseURL: '/api', // 所有请求都会带上/api前缀，然后被代理转发
  timeout: 5000
});

// 注册接口
export const registerService = (userData) => {
  return request.post('/users/register', userData);
}

// 登录接口
export const loginService = (credentials) => {
  return request.post('/users/login', credentials);
}
```



Layout.vue:

```vue
<template>
  <el-container class="layout-container">
    <el-header height="60px">
      <div class="header-content">
        <!-- 核心修改：用 router-link 包裹 logo -->
        <router-link to="/home" class="logo-link">
          <div class="logo">校园二手交易系统</div>
        </router-link>

        <div class="user-info">
          <el-button type="primary" plain size="small" @click="goToPublish">发布商品</el-button>
          <el-button type="success" plain size="small" @click="goToProfile">个人中心</el-button>
          <el-button type="warning" plain size="small" @click="goToMessages">消息中心</el-button>
          <span class="welcome-text">欢迎, {{ username }}</span>
          <el-button type="danger" plain size="small" @click="logout">退出登录</el-button>
        </div>
      </div>
    </el-header>
    <el-main>
      <div class="main-content-wrapper">
        <router-view></router-view>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // 确保引入了 useRouter

const router = useRouter();
const username = ref('');

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (user && user.username) {
    username.value = user.username;
  }
});

const goToPublish = () => {
  router.push('/home/publish');
};
const goToProfile = () => {
  router.push('/home/profile');
};
const goToMessages = () => {
  router.push('/home/messages');
};
const logout = () => {
  localStorage.removeItem('user');
  router.push('/login');
};
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background-color: #f0f2f5;
}

.el-header {
  position: sticky; /* 让头部在滚动时固定在顶部 */
  top: 0;
  z-index: 100;
  background-color: #303133;
  color: #fff;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 核心修改：为 router-link 设置样式 */
.logo-link {
  text-decoration: none; /* 去掉链接的下划线 */
  color: inherit; /* 继承父元素的颜色 (白色) */
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info > .el-button {
  margin-right: 10px;
}

.welcome-text {
  margin: 0 15px;
}

.main-content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}
</style>
```





index.js:

```js
//src/router/
import { createRouter, createWebHistory } from 'vue-router'
// 不再在这里导入 ElMessage

// 导入所有视图组件
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import Layout from '../layouts/Layout.vue'
import HomeView from '../views/HomeView.vue'
import DetailView from '../views/DetailView.vue'
import PublishView from '../views/PublishView.vue'
import ProfileView from '../views/ProfileView.vue'
import MessageCenterView from '../views/MessageCenterView.vue'
import AdminView from '../views/AdminView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/register', name: 'register', component: RegisterView },
    {
      path: '/home',
      component: Layout,
      redirect: '/home/products',
      children: [
        { path: 'products', name: 'home', component: HomeView },
        { path: 'detail/:id', name: 'detail', component: DetailView },
        { path: 'publish', name: 'publish', component: PublishView },
        { path: 'profile', name: 'profile', component: ProfileView },
        { path: 'messages', name: 'messages', component: MessageCenterView }
      ]
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView
    }
  ]
})

// 最终版导航守卫 (已移除 ElMessage)
router.beforeEach((to, from, next) => {
  const userStr = localStorage.getItem('user');
  const user = userStr ? JSON.parse(userStr) : null;

  // 1. 访问管理员页面
  if (to.name === 'admin') {
    if (user && user.username === 'admin') {
      next(); // 是管理员，放行
    } else {
      // ElMessage.error('您没有权限访问此页面！'); // 已移除
      next({ name: 'login' }); // 不是管理员，直接跳转
    }
    return;
  }

  // 2. 访问需要登录的页面 (非登录/注册/管理员页)
  if (to.name !== 'login' && to.name !== 'register') {
    if (user) {
      next(); // 已登录，放行
    } else {
      // ElMessage.error('请先登录！'); // 已移除
      next({ name: 'login' }); // 未登录，直接跳转
    }
    return;
  }

  // 3. 访问公共页面 (登录/注册页)，直接放行
  next();
});


export default router
```







AdminView.vue:

```vue
<template>
  <div class="admin-container">
    <h1>后台管理面板</h1>
    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="admin-tabs">

      <!-- ==================== 汇总统计 ==================== -->
      <el-tab-pane label="汇总统计" name="summary">
        <el-card class="query-card">
          <h3>汇总查询</h3>
          <el-form :model="summaryParams" inline>
            <el-form-item label="关键词">
              <el-input v-model="summaryParams.keyword" placeholder="商品名称" clearable style="width: 150px;" />
            </el-form-item>
            <el-form-item label="分类">
              <el-select v-model="summaryParams.category" placeholder="所有分类" clearable style="width: 130px;">
                <el-option label="书籍教材" value="书籍教材"></el-option>
                <el-option label="电子产品" value="电子产品"></el-option>
                <el-option label="生活用品" value="生活用品"></el-option>
                <el-option label="服饰鞋包" value="服饰鞋包"></el-option>
                <el-option label="运动健身" value="运动健身"></el-option>
                <el-option label="乐器相关" value="乐器相关"></el-option>
                <el-option label="美妆护肤" value="美妆护肤"></el-option>
                <el-option label="其他" value="其他"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="价格区间">
              <el-input-number v-model="summaryParams.minPrice" :min="0" :precision="2" controls-position="right" placeholder="最低价" style="width: 110px;"></el-input-number>
              <span style="margin: 0 5px;">-</span>
              <el-input-number v-model="summaryParams.maxPrice" :min="0" :precision="2" controls-position="right" placeholder="最高价" style="width: 110px;"></el-input-number>
            </el-form-item>
            <el-form-item label="时间范围">
              <el-date-picker
                v-model="summaryParams.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchSummary">查询</el-button>
              <el-button @click="resetSummaryQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-divider><h3>挂牌信息汇总</h3></el-divider>
        <el-table :data="summary.productsSummary" border stripe>
          <el-table-column prop="category" label="物品种类"></el-table-column>
          <el-table-column prop="total_count" label="挂牌总数"></el-table-column>
          <el-table-column prop="on_sale_count" label="在售数量"></el-table-column>
          <el-table-column prop="sold_count" label="已售数量"></el-table-column>
        </el-table>

        <el-divider><h3>成交情况汇总</h3></el-divider>
        <el-table :data="summary.salesSummary" border stripe>
          <el-table-column prop="category" label="物品种类"></el-table-column>
          <el-table-column prop="transaction_count" label="成交笔数"></el-table-column>
          <el-table-column prop="total_turnover" label="成交总额"></el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- ==================== 商品管理 ==================== -->
      <el-tab-pane label="商品管理" name="products">
        <el-table :data="allProducts" stripe border>
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
          <el-table-column prop="sellerId" label="卖家ID" width="100"></el-table-column>
          <el-table-column prop="price" label="价格" width="100"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === '在售' ? 'success' : 'info'">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-popconfirm title="确定要删除这个商品吗？这将一并删除相关订单和留言，且不可恢复！" @confirm="deleteProduct(row.id)">
                <template #reference>
                  <el-button type="danger" size="small">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- ==================== 用户管理 ==================== -->
      <el-tab-pane label="用户管理" name="users">
        <el-card class="query-card">
          <h3>用户查询</h3>
          <el-form :model="userParams" inline>
            <el-form-item label="用户名">
              <el-input v-model="userParams.username" placeholder="用户名" clearable></el-input>
            </el-form-item>
            <el-form-item label="学号">
              <el-input v-model="userParams.studentId" placeholder="学号" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchUsers">查询</el-button>
              <el-button @click="resetUserQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-table :data="userList" stripe border>
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column prop="gender" label="性别" width="80"></el-table-column>
          <el-table-column prop="studentId" label="学号"></el-table-column>
        </el-table>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getSummaryService, deleteProductService, getAllUsersService } from '@/api/admin.js';
import { getProductsService } from '@/api/product.js';
import { ElMessage } from 'element-plus';

const activeTab = ref('summary');

// --- 汇总统计 ---
const defaultSummaryParams = { keyword: '', category: '', minPrice: undefined, maxPrice: undefined, dateRange: [] };
const summaryParams = ref({ ...defaultSummaryParams });
const summary = ref({ productsSummary: [], salesSummary: [] });

/**
 * 【最终版】构建汇总查询的参数，确保所有有效值都被包含
 */
const buildSummaryParams = () => {
  const params = {};
  const { keyword, category, minPrice, maxPrice, dateRange } = summaryParams.value;

  if (keyword) params.keyword = keyword;
  if (category) params.category = category;
  // 只有当 minPrice 是一个数字（包括0）时才添加
  if (typeof minPrice === 'number') params.minPrice = minPrice;
  if (typeof maxPrice === 'number') params.maxPrice = maxPrice;
  if (dateRange && dateRange.length === 2) {
    params.startTime = dateRange[0] + ' 00:00:00';
    params.endTime = dateRange[1] + ' 23:59:59';
  }
  return params;
};

const fetchSummary = async () => {
  try {
    const params = buildSummaryParams();
    console.log("发送给后端的汇总查询参数:", params); // 保留日志用于调试
    const response = await getSummaryService(params);
    summary.value = response.data;
  } catch (error) {
    console.error("获取汇总数据失败:", error);
    ElMessage.error('获取汇总数据失败');
  }
};

const resetSummaryQuery = () => {
  summaryParams.value = { ...defaultSummaryParams };
  fetchSummary();
};

// --- 商品管理 ---
const allProducts = ref([]);
const fetchAllProducts = async () => {
  try {
    const response = await getProductsService();
    allProducts.value = response.data;
  } catch (error) {
    ElMessage.error('获取商品列表失败');
  }
};
const deleteProduct = async (id) => {
  try {
    await deleteProductService(id);
    ElMessage.success('删除成功！');
    fetchAllProducts();
  } catch (error) {
    ElMessage.error('删除失败');
  }
};

// --- 用户管理 ---
const defaultUserParams = { username: '', studentId: '' };
const userParams = ref({ ...defaultUserParams });
const userList = ref([]);
const fetchUsers = async () => {
  try {
    const params = {};
    if (userParams.value.username) params.username = userParams.value.username;
    if (userParams.value.studentId) params.studentId = userParams.value.studentId;
    const response = await getAllUsersService(params);
    userList.value = response.data;
  } catch (error) {
    ElMessage.error('获取用户列表失败');
  }
};
const resetUserQuery = () => {
  userParams.value = { ...defaultUserParams };
  fetchUsers();
};

// --- Tab 切换 ---
const handleTabClick = (tab) => {
  if (tab.props.name === 'summary') fetchSummary();
  else if (tab.props.name === 'products') fetchAllProducts();
  else if (tab.props.name === 'users') fetchUsers();
};

onMounted(() => {
  fetchSummary();
});
</script>

<style scoped>
.admin-container {
  padding: 20px;
}

h1, h3 {
  color: #303133;
  font-weight: 600;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.admin-tabs {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}

.query-card {
  margin-bottom: 20px;
  background-color: #fcfcfc;
  border: 1px solid #e4e7ed;
}

.el-divider {
  margin: 30px 0;
}

:deep(.el-table) {
  color: #606266;
}

:deep(.el-table th) {
  color: #303133;
}
</style>
```







DetailView.vue:

```vue
<template>
  <div v-if="product" class="detail-container">
    <el-page-header @back="goBack" :content="`商品详情: ${product.name}`" />

    <el-row :gutter="20" class="content-row">
      <el-col :span="10">
        <img :src="product.imageUrl || 'https://images.unsplash.com/photo-1517842645767-c6f90405774b?q=80&w=2098&auto=format&fit=crop'" class="product-image-large"/>
      </el-col>

      <el-col :span="14">
        <h1>{{ product.name }}</h1>
        <p class="description">{{ product.description }}</p>
        <el-divider />
        <p><strong>分类:</strong> {{ product.category }}</p>
        <p><strong>状态:</strong>
          <el-tag :type="product.status === '在售' ? 'success' : 'info'">{{ product.status }}</el-tag>
        </p>
        <p class="price-large">价格: ¥ {{ product.price }}</p>
        <el-divider />
        <el-button type="danger" plain @click="purchase">立即购买</el-button>
        <el-button type="primary" @click="startChat">留言咨询</el-button>
      </el-col>
    </el-row>
  </div>

  <div v-else>
    <p>商品加载中或不存在...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProductByIdService } from '@/api/product.js';
import { purchaseService } from '@/api/order.js';
import { sendMessageService } from '@/api/message.js'; // 注意：这里只需要引入发送消息的API
import { ElMessage, ElMessageBox } from 'element-plus';

const route = useRoute();
const router = useRouter();
const product = ref(null);
const currentUser = ref(null);

onMounted(async () => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    currentUser.value = JSON.parse(userStr);
  }

  const productId = route.params.id;
  try {
    const response = await getProductByIdService(productId);
    product.value = response.data;
  } catch (error) {
    console.error("获取商品详情失败", error);
    ElMessage.error("获取商品详情失败");
  }
});

const goBack = () => {
  router.back();
};

const purchase = async () => {
  if (!product.value || !currentUser.value) {
    ElMessage.error('信息错误或未登录！');
    return;
  }
  if (product.value.status !== '在售') {
    ElMessage.warning('商品已售出或下架。');
    return;
  }
  if (currentUser.value.id === product.value.sellerId) {
    ElMessage.warning('不能购买自己发布的商品！');
    return;
  }
  ElMessageBox.confirm(
    `确定要以 ¥ ${product.value.price} 的价格购买 “${product.value.name}” 吗？`,
    '确认购买',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    try {
      const payload = { productId: product.value.id, buyerId: currentUser.value.id };
      await purchaseService(payload);
      ElMessage.success('购买成功！');
      product.value.status = '已售';
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.response?.data || '购买失败';
      ElMessage.error(errorMessage);
    }
  }).catch(() => {
    ElMessage.info('已取消购买');
  });
};

/**
 * 【新】发起聊天并跳转到消息中心的函数
 */
const startChat = async () => {
  if (!currentUser.value || !product.value) {
    ElMessage.error('请先登录或商品信息有误！');
    return;
  }
  // 在 startChat 函数的开头
  if (currentUser.value.id === product.value.sellerId) {
    ElMessage.info('这是您自己发布的商品，无需留言。');
    return;
  }

  // 直接跳转到消息中心。
  // 我们可以在消息中心页面去判断是否需要创建新对话。
  // 为了简化，这里也可以先发送一条默认消息来“创建”对话。
  try {
    const messageData = {
      productId: product.value.id,
      senderId: currentUser.value.id,
      receiverId: product.value.sellerId,
      content: `你好，我对你的商品 “${product.value.name}” 很感兴趣。`
    };
    await sendMessageService(messageData);
    ElMessage.success('已发起对话，正在跳转到消息中心...');
    router.push('/home/messages');
  } catch (error) {
    ElMessage.error('发起对话失败，请稍后重试。');
  }
};
</script>

<style scoped>
/* 样式代码保持不变，这里省略以保持简洁，请保留你原来的样式代码 */
.detail-container { background-color: #fff; padding: 20px; border-radius: 5px; }
.content-row { margin-top: 20px; }
.product-image-large { display: block; width: 100%; height: 400px; object-fit: contain; background-color: #f5f7fa; border-radius: 5px; border: 1px solid #ebeef5; }
h1 { margin-top: 0; color: #303133; font-weight: 600; }
p { color: #606266; line-height: 1.6; }
p > strong { color: #303133; }
.description { margin: 20px 0; }
.price-large { font-size: 24px; font-weight: bold; color: #f56c6c; margin-bottom: 20px; }
:deep(.el-page-header__title),
:deep(.el-page-header__content) { color: #303133 !important; }
:deep(.el-divider--horizontal) { margin: 20px 0; }
</style>
```







HomeView.vue:

```vue
<template>
  <!--
    我们使用 el-row，但去掉 gutter 属性
    我们给它一个 flex 布局和 gap 属性
  -->
  <el-row class="product-row">
    <!--
      我们只设置 el-col 的宽度，间距由父容器的 gap 控制
    -->
    <el-col :span="6" v-for="product in productList" :key="product.id" class="product-col">
      <el-card class="product-card" @click="goToDetail(product.id)">
        <div class="image-container">
          <img :src="product.imageUrl || 'https://images.unsplash.com/photo-1517842645767-c6f90405774b?q=80&w=2098&auto=format&fit=crop'" class="product-image"/>
        </div>
        <div class="info-container">
          <p class="product-name">{{ product.name }}</p>
          <div class="bottom-area">
            <span class="price">¥ {{ product.price }}</span>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
// script 部分保持不变
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProductsService } from '@/api/product.js'

const productList = ref([])
const router = useRouter()

onMounted(async () => {

  try {
    const response = await getProductsService()
    productList.value = response.data
  } catch (error) {
    console.error('获取商品列表失败', error)
  }
})

const goToDetail = (id) => {
  router.push(`/home/detail/${id}`)
}
</script>

<style scoped>
/*
 * 核心修改：重写 el-row 的样式
 */
.product-row {
  display: flex;
  flex-wrap: wrap;
  /* 使用 gap 属性来创建间距，它在高DPI下计算更精确 */
  gap: 20px;
}

.product-col {
  /*
   * 核心修改：使用 CSS 的 calc() 函数来精确计算宽度
   * (100% - (列数-1) * 间距) / 列数
   * (100% - 3 * 20px) / 4
   */
  width: calc((100% - 60px) / 4);
  /*
   * 我们不再需要 Element Plus 的响应式断点，
   * 因为 Flexbox + gap + calc() 已经能完美处理换行和间距
   */
  max-width: none; /* 覆盖 el-col 可能的 max-width */
  flex-basis: auto; /* 覆盖 el-col 可能的 flex-basis */
  flex-grow: 0;
}


/* --- 以下卡片样式保持不变 --- */
.product-card {
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  width: 100%;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.image-container {
  width: 100%;
  padding-top: 75%;
  position: relative;
  overflow: hidden;
  background-color: #eee;
}

.product-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-container {
  padding: 14px;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 44px;
}

.bottom-area {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}
</style>
```



LoginView.vue:

```vue
<template>
  <div class="login-container">
    <el-form :model="form" label-width="80px" class="login-form">
      <h2>用户登录</h2>
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onLogin">登录</el-button>
        <el-button @click="goToRegister">去注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginService } from '@/api/user.js';
import { ElMessage } from 'element-plus';

const router = useRouter();

const form = ref({
  username: '',
  password: ''
});

// 在 onLogin 函数内部
const onLogin = async () => {
  try {
    const response = await loginService(form.value);
    ElMessage.success('登录成功！');

    // 1. 将后端返回的用户信息存储到 localStorage
    // JSON.stringify 是为了把JS对象转成字符串才能存储
    localStorage.setItem('user', JSON.stringify(response.data));


    // 判断是否是管理员
    if (response.data.username === 'admin') {
      router.push('/admin');
    } else {
      router.push('/home');
    }

  } catch (error) {
    ElMessage.error(error.response?.data || '登录失败');
  }
};

const goToRegister = () => {
  router.push('/register');
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5; /* 更柔和的背景色 */
}
.login-form {
  width: 400px;
  padding: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 新增：添加阴影效果，让表单浮起来 */
}
h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #303133; /* 新增：设置清晰的字体颜色 */
  font-weight: 600; /* 新增：字体加粗 */
}
</style>
```





MessageCenterView.vue:

```vue
<template>
  <div class="message-center-container">
    <h2>消息中心</h2>
    <div v-if="conversations.length > 0">
      <el-card v-for="convo in conversations" :key="convo.id" class="convo-card" @click="openChat(convo)">
        <div class="convo-content">
          <div class="left">
            <p class="product-name">关于: {{ convo.productName }}</p>
            <p class="other-party">对话人: {{ convo.otherPartyUsername }}</p>
          </div>
          <div class="right">
            <p class="last-message">{{ convo.content }}</p>
            <p class="time">{{ new Date(convo.created_at).toLocaleString() }}</p>
          </div>
        </div>
      </el-card>
    </div>
    <el-empty v-else description="暂无消息"></el-empty>

    <!-- 复用详情页的聊天对话框 -->
    <el-dialog v-model="chatDialogVisible" :title="`与 ${chatTarget.name} 的对话`" width="50%">
      <div class="message-box">
        <div v-for="msg in messages" :key="msg.id" class="message-item" :class="{ 'is-me': msg.senderId === currentUser.id }">
          <div class="message-content">{{ msg.content }}</div>
        </div>
      </div>
      <el-input v-model="newMessage" type="textarea" :rows="3" placeholder="请输入回复..." style="margin-top: 20px;"></el-input>
      <template #footer>
        <el-button @click="chatDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="replyMessage">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMyConversationsService, getMessagesService, sendMessageService } from '@/api/message.js';
import { ElMessage } from 'element-plus';

const conversations = ref([]);
const currentUser = ref(JSON.parse(localStorage.getItem('user')));

onMounted(async () => {
  if (currentUser.value) {
    const response = await getMyConversationsService(currentUser.value.id);
    conversations.value = response.data;
  }
});

// --- 聊天对话框逻辑 ---
const chatDialogVisible = ref(false);
const messages = ref([]);
const newMessage = ref('');
const chatTarget = ref({}); // 存储当前对话对象

const openChat = async (convo) => {
  const otherPartyId = convo.sender_id === currentUser.value.id ? convo.receiver_id : convo.sender_id;
  chatTarget.value = {
    id: otherPartyId,
    name: convo.otherPartyUsername,
    productId: convo.product_id
  };

  chatDialogVisible.value = true;

  const response = await getMessagesService(convo.product_id, currentUser.value.id, otherPartyId);
  messages.value = response.data;
};

const replyMessage = async () => {
  if (!newMessage.value.trim()) return;
  const messageData = {
    productId: chatTarget.value.productId,
    senderId: currentUser.value.id,
    receiverId: chatTarget.value.id,
    content: newMessage.value,
  };
  await sendMessageService(messageData);
  newMessage.value = '';
  // 刷新当前对话
  const response = await getMessagesService(chatTarget.value.productId, currentUser.value.id, chatTarget.value.id);
  messages.value = response.data;
};
</script>

<style scoped>
.message-center-container {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
}
/* 新增：设置标题颜色 */
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 600;
}
.convo-card { margin-bottom: 15px; cursor: pointer; }
.convo-card:hover { background-color: #f5f7fa; }
.convo-content { display: flex; justify-content: space-between; align-items: center; }
.product-name { font-weight: bold; }
.other-party, .time { font-size: 14px; color: #909399; }
.last-message { max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
/* 聊天对话框的样式 */
.message-box { height: 300px; overflow-y: auto; border: 1px solid #e4e7ed; padding: 10px; border-radius: 4px; }
.message-item { margin-bottom: 15px; max-width: 80%; }
.message-item .message-content { display: inline-block; padding: 10px 15px; border-radius: 10px; background-color: #f0f2f5; color: #303133; word-break: break-all; }
.message-item.is-me { margin-left: auto; text-align: right; }
.message-item.is-me .message-content { background-color: #409eff; color: #fff; }
</style>
```





ProfileView.vue:

```vue
<template>
  <div>
    <!-- 【核心】这里是完整的查询表单HTML代码 -->
    <el-card class="query-card">
      <h3>{{ activeTab === 'published' ? '我发布的商品查询' : '我买到的订单查询' }}</h3>
      <el-form :model="queryParams" inline>
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="商品名称" clearable @clear="handleQuery" style="width: 150px;" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryParams.category" placeholder="所有分类" clearable @change="handleQuery" style="width: 130px;">
            <el-option label="书籍教材" value="书籍教材"></el-option>
            <el-option label="电子产品" value="电子产品"></el-option>
            <el-option label="生活用品" value="生活用品"></el-option>
            <el-option label="服饰鞋包" value="服饰鞋包"></el-option>
            <el-option label="运动健身" value="运动健身"></el-option>
            <el-option label="乐器相关" value="乐器相关"></el-option>
            <el-option label="美妆护肤" value="美妆护肤"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格区间">
          <el-input-number v-model="queryParams.minPrice" :min="0" controls-position="right" placeholder="最低价" style="width: 110px;"></el-input-number>
          <span style="margin: 0 5px;">-</span>
          <el-input-number v-model="queryParams.maxPrice" :min="0" controls-position="right" placeholder="最高价" style="width: 110px;"></el-input-number>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryParams.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="profile-tabs">
      <el-tab-pane label="我发布的" name="published">
        <el-table :data="myProducts" stripe style="width: 100%">
          <el-table-column prop="name" label="商品名称" width="250"></el-table-column>
          <el-table-column prop="price" label="价格" width="100"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === '在售' ? 'success' : 'info'">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="发布时间">
            <template #default="{ row }">
              <span>{{ formatDateTime(row.created_at) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="我买到的" name="purchased">
        <el-table :data="myOrders" stripe style="width: 100%">
          <el-table-column prop="product_name" label="商品名称" width="250"></el-table-column>
          <el-table-column prop="product_category" label="分类" width="150"></el-table-column>
          <el-table-column prop="order_price" label="成交价" width="100"></el-table-column>
          <el-table-column label="购买时间">
            <template #default="{ row }">
              <span>{{ formatDateTime(row.created_at) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMyProductsService } from '@/api/product.js';
import { getMyOrdersService } from '@/api/order.js';
import { ElMessage } from 'element-plus';

const activeTab = ref('published');
const myProducts = ref([]);
const myOrders = ref([]);
const currentUser = ref(null);

const defaultQueryParams = {
  keyword: '',
  category: '',
  minPrice: undefined,
  maxPrice: undefined,
  dateRange: []
};
const queryParams = ref({ ...defaultQueryParams });

const buildParams = () => {
  const params = {};
  if (queryParams.value.keyword) params.keyword = queryParams.value.keyword;
  if (queryParams.value.category) params.category = queryParams.value.category;
  if (queryParams.value.minPrice) params.minPrice = queryParams.value.minPrice;
  if (queryParams.value.maxPrice) params.maxPrice = queryParams.value.maxPrice;
  if (queryParams.value.dateRange && queryParams.value.dateRange.length) {
    params.startTime = queryParams.value.dateRange[0] + ' 00:00:00'; // 加上时分秒，查询更精确
    params.endTime = queryParams.value.dateRange[1] + ' 23:59:59';
  }
  return params;
};

const handleQuery = () => {
  if (activeTab.value === 'published') {
    fetchMyProducts();
  } else {
    fetchMyOrders();
  }
};

const resetQuery = () => {
  queryParams.value = { ...defaultQueryParams };
  handleQuery();
};

const fetchMyProducts = async () => {
  if (currentUser.value && currentUser.value.id) {
    try {
      const params = buildParams();
      const response = await getMyProductsService(currentUser.value.id, params);
      myProducts.value = response.data;
    } catch (error) {
      console.error("获取已发布商品失败:", error);
      ElMessage.error("获取已发布商品失败，请稍后重试。");
    }
  }
};

const fetchMyOrders = async () => {
  if (currentUser.value && currentUser.value.id) {
    try {
      const params = buildParams();
      const response = await getMyOrdersService(currentUser.value.id, params);
      myOrders.value = response.data;
    } catch (error) {
      console.error("获取已购买订单失败:", error);
      ElMessage.error("获取已购买订单失败，请稍后重试。");
    }
  }
};

onMounted(() => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    currentUser.value = JSON.parse(userStr);
    handleQuery();
  }
});

const handleTabClick = () => {
  resetQuery(); // 切换tab时，重置查询条件并重新加载数据
};

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return 'N/A';
  const date = new Date(dateTimeString);
  return date.toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-');
};
</script>

<style scoped>
.query-card {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 10px 20px 0 20px;
  border-radius: 5px;
}

.profile-tabs {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}

:deep(.el-table) {
  color: #606266;
}

:deep(.el-table th) {
  color: #303133;
}
</style>
```



PublishView.vue:

```vue
<template>
  <div class="publish-container">
    <h2>发布我的二手商品</h2>
    <el-form :model="form" label-width="120px" style="max-width: 600px; margin: 0 auto;">
      <el-form-item label="商品名称">
        <el-input v-model="form.name" placeholder="请输入商品名称"></el-input>
      </el-form-item>
      <el-form-item label="商品分类">
        <el-select v-model="form.category" placeholder="请选择分类">
          <el-option label="书籍教材" value="书籍教材"></el-option>
          <el-option label="电子产品" value="电子产品"></el-option>
          <el-option label="生活用品" value="生活用品"></el-option>
          <el-option label="服饰鞋包" value="服饰鞋包"></el-option>
          <el-option label="运动健身" value="运动健身"></el-option>
          <el-option label="乐器相关" value="乐器相关"></el-option>
          <el-option label="美妆护肤" value="美妆护肤"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品价格">
        <el-input-number v-model="form.price" :precision="2" :step="1" :min="0.01"></el-input-number>
      </el-form-item>
      <el-form-item label="商品描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="详细描述一下你的宝贝吧..."></el-input>
      </el-form-item>
      <el-form-item label="图片链接">
        <el-input v-model="form.imageUrl" placeholder="请输入图片URL (仅为演示)"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">确认发布</el-button>
        <el-button @click="goBack">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { publishProductService } from '@/api/product.js'; // 需要在api/product.js中添加
import { ElMessage } from 'element-plus';

const router = useRouter();

const form = ref({
  name: '',
  category: '书籍教材', // 默认值
  price: 0.01,
  description: '',
  imageUrl: '',
  sellerId: null // 稍后从localStorage获取
});

const onSubmit = async () => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (!user) {
    ElMessage.error('请先登录！');
    router.push('/login');
    return;
  }
  form.value.sellerId = user.id;

  try {
    await publishProductService(form.value);
    ElMessage.success('商品发布成功！');
    router.push('/home'); // 发布成功后跳转回主页
  } catch (error) {
    ElMessage.error('发布失败，请检查输入信息。');
  }
};

const goBack = () => {
  router.back();
};
</script>

<style scoped>
.publish-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}
/* 新增：设置标题颜色 */
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 600;
}
</style>
```







RegisterView.vue:

```vue
<template>
  <div class="register-container">
    <el-form :model="form" label-width="80px" class="register-form">
      <h2>用户注册</h2>
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="学号">
        <el-input v-model="form.studentId"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio label="男"></el-radio>
          <el-radio label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onRegister">注册</el-button>
        <el-button @click="goToLogin">返回登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { registerService } from '@/api/user.js';
import { ElMessage } from 'element-plus';

const router = useRouter();

const form = ref({
  username: '',
  password: '',
  studentId: '',
  gender: '男'
});

const onRegister = async () => {
  try {
    await registerService(form.value);
    ElMessage.success('注册成功！将跳转到登录页。');
    router.push('/login');
  } catch (error) {
    ElMessage.error(error.response?.data || '注册失败');
  }
};

const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5; /* 更柔和的背景色 */
}
.register-form {
  width: 400px;
  padding: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 新增：添加阴影效果，让表单浮起来 */
}
h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #303133; /* 新增：设置清晰的字体颜色 */
  font-weight: 600; /* 新增：字体加粗 */
}
</style>
```





App.vue:

```vue
<template>
  <router-view></router-view>
</template>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: #f0f2f5 !important;
}
</style>
```





main.js:

```js
// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import './assets/main.css'

const app = createApp(App)

app.use(router)
app.use(ElementPlus) // 使用Element Plus

app.mount('#app')
```





package.json:

```json
{
  "name": "campus-trade-frontend",
  "version": "0.0.0",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint . --fix",
    "format": "prettier --write src/"
  },
  "dependencies": {
    "axios": "^1.10.0",
    "element-plus": "^2.10.2",
    "vue": "^3.5.13",
    "vue-router": "^4.5.0"
  },
  "devDependencies": {
    "@eslint/js": "^9.22.0",
    "@vitejs/plugin-vue": "^5.2.3",
    "@vue/eslint-config-prettier": "^10.2.0",
    "eslint": "^9.22.0",
    "eslint-plugin-vue": "~10.0.0",
    "globals": "^16.0.0",
    "prettier": "3.5.3",
    "vite": "^6.2.4",
    "vite-plugin-vue-devtools": "^7.7.2"
  }
}
```





vite.config.js:

```js
// vite.config.js
import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 代理配置
  server: {
    proxy: {
      '/api': { // 捕获所有以 /api 开头的请求
        target: 'http://localhost:8080', // 转发到后端地址
        changeOrigin: true, // 必须设置为true
        // rewrite: (path) => path.replace(/^\/api/, '') // 如果后端接口本身没有/api前缀，就需要这行去掉/api
      }
    }
  }
})
```





