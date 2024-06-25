package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserProfileVO {

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
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户注册时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 用户签名
     */
    private String autograph;

    /**
     * 积分
     */
    private Integer point;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 归属地
     */
    private String address;
}
