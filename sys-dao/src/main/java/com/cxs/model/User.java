package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户信息表
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

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
    private LocalDateTime createTime;

    /**
     * 用户修改时间
     */
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
     * 积分
     */
    private Integer point;

    /**
     * 性别 1、男 2、女
     */
    private Integer sex;

    /**
     * 用户类型 1、新用户 2、老用户
     */
    private Integer userType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}