package com.hitchater.chatapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitchater.chatapp.entity.Message;
import com.hitchater.chatapp.mapper.MessageMapper;
import com.hitchater.chatapp.mapper.UserMapper;
import com.hitchater.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void sendMessage(Message message) {

        // 检查发送者是否存在
        if (userMapper.selectById(message.getSenderId()) == null) {
            throw new IllegalArgumentException("Sender does not exist!");
        }
        // 检查接收者是否存在
        if (userMapper.selectById(message.getReceiverId()) == null) {
            throw new IllegalArgumentException("Receiver does not exist!");
        }
        message.setTimestamp(LocalDateTime.now());
        messageMapper.insert(message);
    }


    @Override
    public List<Message> getMessagesBetweenUsers(String userId1, String userId2) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .and(wrapper -> wrapper.eq("sender_id", userId1).eq("receiver_id", userId2))
                .or(wrapper -> wrapper.eq("sender_id", userId2).eq("receiver_id", userId1))
                .orderByAsc("timestamp");
        return messageMapper.selectList(queryWrapper);
    }
}
