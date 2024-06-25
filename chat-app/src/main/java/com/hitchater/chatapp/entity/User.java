package com.hitchater.chatapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class User {
    @TableId("user_id")
    private String user_id;
    private String user_name;
    private String password;
    private String avatar; // 新增的头像属性

    public String  getUsername() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
