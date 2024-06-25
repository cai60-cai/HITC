package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 菜单权限表
 * @TableName t_menu
 */
@TableName(value ="t_menu")
@Data
public class Menu implements Serializable {
    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Integer menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String menuPath;

    /**
     * 菜单组件
     */
    private String menuComponent;

    /**
     * 重定向地址，在面包屑中点击会重定向去的地址
     */
    private String menuRedirect;

    /**
     * 是否在侧边栏显示,1、不显示、0、显示
     */
    private Integer menuHidden;

    /**
     * 一直显示根路由,1、显示，0、不显示
     */
    private Integer menuAlwaysShow;

    /**
     * 开发者配置项
     */
    private String menuMeta;

    /**
     * 父菜单id
     */
    private Integer menuParantId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}