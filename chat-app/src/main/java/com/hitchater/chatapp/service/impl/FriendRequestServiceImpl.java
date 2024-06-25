package com.hitchater.chatapp.service.impl;

import com.hitchater.chatapp.entity.Friend;
import com.hitchater.chatapp.entity.FriendRequest;
import com.hitchater.chatapp.entity.User;
import com.hitchater.chatapp.mapper.FriendMapper;
import com.hitchater.chatapp.mapper.FriendRequestMapper;
import com.hitchater.chatapp.mapper.UserMapper;
import com.hitchater.chatapp.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FriendMapper friendMapper;

    public String sendFriendRequest(String senderId, String receiverId) {
        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            return "Sender does not exist!";
        }

        User receiver = userMapper.selectById(receiverId);
        if (receiver == null) {
            return "Receiver does not exist!";
        }

        FriendRequest existingRequest = friendRequestMapper.selectBySenderAndReceiver(senderId, receiverId);
        if (existingRequest != null) {
            return "Friend request already sent!";
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderId(senderId);
        friendRequest.setReceiverId(receiverId);
        friendRequest.setSenderUsername((String) sender.getUsername());
        friendRequest.setAccepted(false);
        friendRequestMapper.insert(friendRequest);
        return "Friend request sent!";
    }

    @Override
    public String acceptFriendRequest(Integer requestId) {
        FriendRequest friendRequest = friendRequestMapper.selectById(requestId);
        if (friendRequest == null) {
            return "Friend request not found!";
        }
        friendRequest.setAccepted(true);
        friendRequestMapper.updateById(friendRequest);

        // Add friend relationship in both directions
        Friend friend1 = new Friend();
        friend1.setUserId(friendRequest.getSenderId());
        friend1.setFriendId(friendRequest.getReceiverId());
        friendMapper.insert(friend1);

        Friend friend2 = new Friend();
        friend2.setUserId(friendRequest.getReceiverId());
        friend2.setFriendId(friendRequest.getSenderId());
        friendMapper.insert(friend2);

        return "Friend request accepted!";
    }


    @Override
    public List<FriendRequest> getFriendRequests(String userId) {
        return friendRequestMapper.getFriendRequestsWithUsername(userId);
    }
}
