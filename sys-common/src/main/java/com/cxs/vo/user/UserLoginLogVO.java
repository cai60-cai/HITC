package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserLoginLogVO {

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录地址
     */
    private String loginAddress;
}
