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