package com.cxs.vo.admin.menu;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 重定向地址，在面包屑中点击会重定向去的地址
     */
    private String redirect;

    /**
     * 不在侧边栏显示,
     */
    private Boolean hidden;

    /**
     * 一直显示根路由
     */
    private Boolean alwaysShow;

    /**
     * 开发者配置项
     */
    private JSONObject meta;


    private Integer parentId;

    /**
     * 子菜单
     */
    private List<AdminMenuVO> children = new ArrayList<>();
}
