package com.example.campustrade.mapper;

import com.example.campustrade.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {

    
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


    //返回类型是List<Map<String, Object>>，
    // 因为统计结果的列（如total_count, on_sale_count）在Product实体类中不存在，
    // 所以用一个通用的Map来接收每一行数据会更方便。

    @Select("CALL sp_get_my_products(#{sellerId}, #{keyword}, #{category}, #{minPrice}, #{maxPrice}, #{startTime}, #{endTime})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String, Object>> findBySellerIdWithConditions(Map<String, Object> params);
    //方法签名: 定义Java方法本身,String: Map的键，对应于结果集中每一列的列名。
    // Object: Map的值，对应于该列在那一行中的具体值。

    @Select("CALL sp_get_products_summary(#{keyword}, #{category}, #{minPrice}, #{maxPrice}, #{startTime}, #{endTime})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String, Object>> getProductsSummary(Map<String, Object> params);//返回类型是List<Map<String, Object>>

    @Select("CALL sp_get_sales_summary(#{keyword}, #{category}, #{minPrice}, #{maxPrice}, #{startTime}, #{endTime})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String, Object>> getSalesSummary(Map<String, Object> params);

    // #{...} 语法告诉MyBatis，这里需要使用一个外部传入的参数来替换。
}