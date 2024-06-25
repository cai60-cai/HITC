package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

import java.util.List;


@Data
public class UserVO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户注册时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 用户修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 用户签名
     */
    private String autograph;

    /**
     * 用户状态,1、正常 2、锁定
     */
    private Integer userStatus;

    /**
     * 用户状态
     */
    private Integer userType;

    /**
     * 角色描述
     */
    private List<String> roles;

}
