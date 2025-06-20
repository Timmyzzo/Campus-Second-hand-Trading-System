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