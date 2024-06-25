package com.hitchater.chatapp.controller;

import com.hitchater.chatapp.entity.Friend;
import com.hitchater.chatapp.entity.FriendRequest;
import com.hitchater.chatapp.entity.Message;
import com.hitchater.chatapp.entity.User;
import com.hitchater.chatapp.service.FriendRequestService;
import com.hitchater.chatapp.service.FriendService;
import com.hitchater.chatapp.service.MessageService;
import com.hitchater.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3002")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FriendService friendService;


    @PostMapping("/login")
    public User login(@RequestBody User user) {
        System.out.println("sssssssssssddddddddddddssssss");
        System.out.println(user);
        System.out.println("ssssssssssssddddddddddddsssss");
        User loggedInUser = userService.login(user);
        if (loggedInUser != null) {
            return loggedInUser;
        } else {
            throw new RuntimeException("Invalid credentials!");
        }
    }

    @GetMapping("/search")
    public List<User> searchByUsername(@RequestParam String user_name) {
        return userService.searchByUsername(user_name);
    }

    @PostMapping("/sendFriendRequest")
    public String sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        return friendRequestService.sendFriendRequest(friendRequest.getSenderId(), friendRequest.getReceiverId());
    }

    @PostMapping("/acceptFriendRequest")
    public String acceptFriendRequest(@RequestBody Map<String, Integer> request) {
        Integer requestId = request.get("requestId");
        if (requestId == null) {
            throw new IllegalArgumentException("Request ID is required");
        }
        return friendRequestService.acceptFriendRequest(requestId);
    }


    @GetMapping("/friendRequests")
    public List<FriendRequest> getFriendRequests(@RequestParam String userId) {
        return friendRequestService.getFriendRequests(String.valueOf(userId));
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
        return "Message sent successfully!";
    }

    @GetMapping("/messages")
    public List<Message> getMessagesBetweenUsers(@RequestParam String userId1, @RequestParam String userId2) {
        return messageService.getMessagesBetweenUsers(userId1, userId2);
    }

    @GetMapping("/{user_id}")
    public User getUserById(@PathVariable String user_id) {
        System.out.println("ssssssssssssssdsasdfdsssssssssssssssssasrrrrrrrrrrsss");
        return userService.getUserById(user_id);
    }

    @GetMapping("/friends/{user_id}")
    public List<Friend> getFriends(@PathVariable String user_id) {
        return friendService.getFriends(user_id);
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestBody Map<String, Integer> request) {
        String userId = String.valueOf(request.get("userId"));
        String friendId = String.valueOf(request.get("friendId"));
        if (userId == null || friendId == null) {
            throw new IllegalArgumentException("User ID and Friend ID are required");
        }
        return friendService.addFriend(userId, friendId);
    }

    @PostMapping("/uploadAvatar/{userId}")
    public ResponseEntity<?> uploadAvatar(@PathVariable String userId, @RequestParam("avatar") MultipartFile file) {
        try {
            String avatarUrl = userService.saveUserAvatar(userId, file);
            User updatedUser = userService.getUserById(userId);
            updatedUser.setAvatar(avatarUrl); // 设置用户的头像URL
            userService.updateUser(updatedUser); // 更新用户信息
            return ResponseEntity.ok(updatedUser); // 返回更新后的用户数据
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
