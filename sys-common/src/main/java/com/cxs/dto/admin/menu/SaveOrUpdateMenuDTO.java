package com.cxs.dto.admin.menu;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveOrUpdateMenuDTO {
    private Integer menuId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "name为必传项")
    @ParamLengthValid(max = 50, message = "name上限50字符")
    private String name;

    /**
     * 菜单路径
     */
    @NotBlank(message = "path为必传项")
    @ParamLengthValid(max = 100, message = "name上限100字符")
    private String path;

    /**
     * 菜单组件
     */
    @ParamLengthValid(max = 100, message = "component上限100字符")
    private String component;

    /**
     * 重定向地址，在面包屑中点击会重定向去的地址
     */
    @ParamLengthValid(max = 100, message = "redirect上限100字符")
    private String redirect;

    /**
     * 是否在侧边栏显示,1、不显示、0、显示
     */
    @NotNull(message = "hidden为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "范围限制为{0, 1}")
    private Integer hidden;

    /**
     * 一直显示根路由,1、显示，0、不显示
     */
    @NotNull(message = "alwaysShow为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "范围限制为{0, 1}")
    private Integer alwaysShow;

    /**
     * 开发者配置项
     */
    private String meta;

    /**
     * 父菜单id
     */
    private Integer menuParantId = -1;
}
