-- 创建表结构

-- 用户表 (users)
CREATE TABLE `users` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID, 主键', -- 自增长
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名, 非空, 唯一',
  `password` VARCHAR(255) NOT NULL COMMENT '密码, 非空',
  `gender` VARCHAR(10) NOT NULL COMMENT '性别, 非空',
  `student_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '学号, 非空, 唯一',
  `balance` DECIMAL(10, 2) NOT NULL DEFAULT 10000.00 COMMENT '余额, 非空, 默认10000', -- 数据类型为定点小数，共10位，小数点后2位。
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  CONSTRAINT `chk_gender` CHECK (`gender` IN ('男', '女'))-- 定义约束
) COMMENT='用户信息表';

-- 商品表 (products)
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
    -- 强制要求products.seller_id的值必须是users表中一个已经存在的id。
  CONSTRAINT `chk_price` CHECK (`price` > 0),
  CONSTRAINT `chk_status` CHECK (`status` IN ('在售', '已售', '已下架'))
) COMMENT='商品信息表';

--  订单表 (orders)
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

-- 消息表 (messages)
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



-- 视图：v_orders_details (简化订单详情查询)
CREATE VIEW `v_orders_details` AS
SELECT 
    o.id AS order_id, -- 别名
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
        -- NEW是一个特殊的关键字，代表即将被插入的这一行新数据
    END IF;
END$$
DELIMITER ;

-- (3) 存储过程：sp_purchase_product (处理购买逻辑)
DELIMITER $$
CREATE PROCEDURE `sp_purchase_product`(IN p_product_id INT, IN p_buyer_id INT)
    -- 接收2个参数：商品ID和买家ID
BEGIN
    --  声明三个局部变量，用于存储从products表中查询出来的信息：
    DECLARE v_seller_id INT;
    DECLARE v_price DECIMAL(10, 2);
    DECLARE v_product_status VARCHAR(20);

    -- 错误发生时，事务都会被回滚
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    START TRANSACTION;

    SELECT seller_id, price, status INTO v_seller_id, v_price, v_product_status
                                    FROM products WHERE id = p_product_id FOR UPDATE;
    -- 对查询到的行加排他锁

    IF v_product_status != '在售' THEN ROLLBACK; SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '商品不是在售状态'; END IF;
    
    UPDATE products SET status = '已售' WHERE id = p_product_id;

    INSERT INTO orders (product_id, buyer_id, seller_id, order_price) VALUES (p_product_id, p_buyer_id, v_seller_id, v_price);

    COMMIT;
END$$
DELIMITER ;


-- 存储过程：sp_get_users (查询用户，带条件)
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
        student_id as studentId
    FROM 
        users
    WHERE
        -- 允许调用者选择性地传入参数
        (p_username IS NULL OR p_username = '' OR username LIKE CONCAT('%', p_username, '%'))
      -- 不为空且不为NULL，则执行模糊匹配
        AND 
        (p_studentId IS NULL OR p_studentId = '' OR student_id = p_studentId);
END$$
DELIMITER ;

-- 存储过程：sp_get_my_products (查询“我发布的”商品，带复杂条件)
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
        seller_id = p_sellerId -- 强制条件，必须传入sellerId才能查询到该卖家的商品。
        AND (p_keyword IS NULL OR name LIKE CONCAT('%', p_keyword, '%'))
        -- 关键词模糊搜索：如果p_keyword为NULL则忽略此条件，否则对name字段进行模糊匹配。
        AND (p_category IS NULL OR category = p_category)
        AND (p_minPrice IS NULL OR price >= p_minPrice)
        AND (p_maxPrice IS NULL OR price <= p_maxPrice)
        AND (p_startTime IS NULL OR created_at >= p_startTime)
        AND (p_endTime IS NULL OR created_at <= p_endTime)
    ORDER BY created_at DESC;-- 查询结果按照商品的创建时间（created_at）降序排列
END$$
DELIMITER ;

-- 存储过程：sp_get_products_summary (查询“挂牌信息汇总”，带复杂条件)
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
        category, -- 按分类进行分组，显示每个分组的分类名称。
        COUNT(*) as total_count, -- 统计当前分组中所有商品的数量
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
    GROUP BY p.category;-- 将满足WHERE条件的所有行按照`category`字段进行分组
END$$
DELIMITER ;

-- 存储过程：sp_get_sales_summary (查询“成交情况汇总”，带复杂条件)
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
        COUNT(o.id) as transaction_count, -- 统计每个分类下的订单数量（即交易笔数），并命名为`transaction_count`。
        SUM(o.order_price) as total_turnover -- 统计每个分类下所有订单的总价格（即总成交金额），并命名为`total_turnover`。
    FROM orders o 
    JOIN products p ON o.product_id = p.id
    WHERE
        (p_keyword IS NULL OR p.name LIKE CONCAT('%', p_keyword, '%'))-- 拼接多个字符串。
        AND (p_category IS NULL OR p.category = p_category)
        AND (p_minPrice IS NULL OR p.price >= p_minPrice)
        AND (p_maxPrice IS NULL OR p.price <= p_maxPrice)
        AND (p_startTime IS NULL OR o.created_at >= p_startTime)
        AND (p_endTime IS NULL OR o.created_at <= p_endTime)
    GROUP BY p.category;
END$$
DELIMITER ;