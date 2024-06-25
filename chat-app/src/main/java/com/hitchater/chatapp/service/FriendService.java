package com.hitchater.chatapp.service;

import com.hitchater.chatapp.entity.Friend;

import java.util.List;

public interface FriendService {
    String addFriend(String userId, String friendId);
    List<Friend> getFriends(String user_id);

}
