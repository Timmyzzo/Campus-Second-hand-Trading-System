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