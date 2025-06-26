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
        // 接收Message对象，调用messageService.sendMessage
        boolean success = messageService.sendMessage(message);
        if (success) {
            return ResponseEntity.ok("留言成功！");
        }
        return ResponseEntity.badRequest().body("留言失败！");
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(
            //使用@RequestParam接收productId, userId1, userId2，查询两个用户间的对话
            @RequestParam Integer productId,
            @RequestParam Integer userId1,
            @RequestParam Integer userId2) {
        List<Message> messages = messageService.getMessages(productId, userId1, userId2);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/my-conversations")//使用@RequestParam接收当前用户的userId，获取他的所有对话列表
    public ResponseEntity<List<Map<String, Object>>> getMyConversations(@RequestParam Integer userId) {
        List<Map<String, Object>> conversations = messageMapper.findMyConversations(userId);
        return ResponseEntity.ok(conversations);
    }
}