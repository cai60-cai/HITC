package com.hitchater.chatapp.service.impl;

import com.hitchater.chatapp.entity.Friend;
import com.hitchater.chatapp.mapper.FriendMapper;
import com.hitchater.chatapp.mapper.UserMapper;
import com.hitchater.chatapp.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendMapper friendMapper;
    private UserMapper userMapper;

    public FriendServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String addFriend(String userId, String friendId) {
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friendMapper.insert(friend);
        return "Friend added successfully!";
    }

    @Override
    public List<Friend> getFriends(String user_id) {
            return friendMapper.getFriendsWithUsername(user_id);
        }


}
