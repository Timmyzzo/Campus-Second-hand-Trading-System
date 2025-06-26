package com.example.campustrade.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider; // 注意：我们换用 SelectProvider
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    //使用 SelectProvider 动态构建对 v_orders_details 视图的查询
    @SelectProvider(type = OrderSqlBuilder.class, method = "buildFindByConditions")
    List<Map<String, Object>> findByConditions(Map<String, Object> params);
    //接收前端传来的所有查询参数（被封装在params这个Map里）
}