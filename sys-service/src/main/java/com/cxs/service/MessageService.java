package com.cxs.service;

import com.cxs.dto.MessageRequest;
import com.cxs.model.Message;
import jdk.jfr.BooleanFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(MessageRequest messageRequest) {
        Message message = new Message();
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setContent(messageRequest.getContent());
        messageRepository.save(message);
    }
}
