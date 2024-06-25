package com.hitchater.chatapp.service;

import com.hitchater.chatapp.entity.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.IOException;
import java.util.List;

public interface UserService {

    User login(User user); // 修改返回值为 User
    List<User> searchByUsername(String user_name);
    User getUserById(String user_id);
    String saveUserAvatar(String userId, MultipartFile file);
    void updateUser(User user); // 新增方法
}
