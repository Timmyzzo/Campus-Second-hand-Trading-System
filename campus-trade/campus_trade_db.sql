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