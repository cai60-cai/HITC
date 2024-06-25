package com.hitchater.chatapp.service;

import com.hitchater.chatapp.entity.Message;

import java.util.List;

public interface MessageService {
    void sendMessage(Message message);
    List<Message> getMessagesBetweenUsers(String userId1, String userId2);

}
