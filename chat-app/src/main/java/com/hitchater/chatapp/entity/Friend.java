package com.hitchater.chatapp.entity;

public class Friend {
    private Integer id;
    private String userId;
    private String friendId;
    private String user_name; // 新增字段

    private String avatar; // 新增字段

    // getters and setters for all fields, including the new field
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String user_name) {
        this.user_name = user_name;
    }

}
