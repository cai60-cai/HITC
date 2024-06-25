package com.hitchater.chatapp.controller;

import com.hitchater.chatapp.entity.Friend;
import com.hitchater.chatapp.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/list")
    public List<Friend> getFriends(@RequestParam String userId) {
        return friendService.getFriends(userId);
    }
}
