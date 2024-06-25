package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * nav-top链接表
 * @TableName t_nav_link
 */
@TableName(value ="t_nav_link")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavLink implements Serializable {
    /**
     * 链接id
     */
    @TableId(type = IdType.AUTO)
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
     * 是否需要登录查看
     */
    private Integer navMustLogin;

    /**
     * 鼠标移入说明
     */
    private String navDesc;

    /**
     * 备注
     */
    private String navRemark;

    /**
     * 链接顺序
     */
    private Integer navOrder;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}