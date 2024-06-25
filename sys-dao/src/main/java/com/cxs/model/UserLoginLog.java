package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录流水表
 * @TableName t_user_login_log
 */
@TableName(value ="t_user_login_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginLog implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer logId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 登录方式
     */
    private Integer loginMode;

    /**
     * 用户登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录地址
     */
    private String loginAddress;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}