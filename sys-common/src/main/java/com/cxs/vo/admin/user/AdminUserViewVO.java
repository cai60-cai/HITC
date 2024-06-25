package com.cxs.vo.admin.user;

import lombok.Data;


@Data
public class AdminUserViewVO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatar;
}
