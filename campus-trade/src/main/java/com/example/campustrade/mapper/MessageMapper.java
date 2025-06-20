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