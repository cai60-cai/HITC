package com.hitchater.chatapp.service;

import com.hitchater.chatapp.entity.FriendRequest;

import java.util.List;

public interface FriendRequestService {
    String sendFriendRequest(String senderId, String receiverId);
    String acceptFriendRequest(Integer requestId);
    List<FriendRequest> getFriendRequests(String userId);

}
