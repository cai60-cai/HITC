package com.cxs.vo.admin;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.vo.admin.menu.AdminMenuInfoVO;
import com.cxs.vo.user.UserSettingVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class AdminUserVO {

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
     * 角色
     */
    private List<String> roles;

    /**
     * 菜单信息
     */
    private AdminMenuInfoVO menuInfo;

    private Integer userType;
}
