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