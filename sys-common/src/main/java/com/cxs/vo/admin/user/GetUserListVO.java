package com.cxs.vo.admin.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class GetUserListVO {
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
}
