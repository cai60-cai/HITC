package com.hitchater.chatapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hitchater.chatapp.entity.User;
import com.hitchater.chatapp.mapper.UserMapper;
import com.hitchater.chatapp.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;




    @Autowired
    private PasswordEncoder passwordEncoder; // 注入 PasswordEncoder

    @Override
    public User login(User user) {
        // 根据用户名查询用户
        User foundUser = userMapper.selectByUsername( user.getUsername());

        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            // 如果用户存在并且密码匹配，返回用户信息
            return foundUser;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }


    @Override
    public List<User> searchByUsername(String user_name) {
        return userMapper.searchByUsername(user_name);
    }

    @Override
    public User getUserById(String user_id) {
        System.out.println("sssssssssssssssss");
        System.out.println("sssssssssssssssss");
        System.out.println("sssssssssssssssss");
        System.out.println("sssssssssssssssss");
        System.out.println("sssssssssssssssss");
        System.out.println(userMapper.selectById(user_id));
        return userMapper.selectById(user_id);
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveUserAvatar(String userId, MultipartFile file) {
        String uploadDir = "D:\\cxs-currency-sys-server-master\\upload";
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadDir + fileName);
//        String fileName = file.getOriginalFilename();
//        Path filePath = Paths.get(uploadDir, fileName);
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file");
        }

        String avatarUrl = "D:\\cxs-currency-sys-server-master\\upload\\20240621" + fileName;
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);

        return avatarUrl;
    }
    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

}
