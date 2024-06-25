package com.cxs.controller;

import com.cxs.dto.MessageRequest;
import com.cxs.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) {
        messageService.sendMessage(messageRequest);
        return ResponseEntity.ok("Message sent successfully");
    }
}
