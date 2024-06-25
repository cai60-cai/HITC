package com.cxs.vo.main;

import lombok.Data;

import java.io.Serializable;


@Data
public class NavLinkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 链接id
     */
    private Integer navId;

    /**
     * 链接名称
     */
    private String navName;

    /**
     * 链接/路由地址
     */
    private String navLink;

    /**
     * 链接类型, 1、路由 2、链接
     */
    private Integer navType;

    /**
     * 链接图标
     */
    private String navIcon;

    /**
     * 鼠标移入说明
     */
    private String navDesc;

    /**
     * 备注
     */
    private String navRemark;

    /**
     * 是否需要登录查看
     */
    private Integer navMustLogin;
}
