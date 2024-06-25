package com.cxs.dto.admin.nav;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class CreateOrUpdateNavLinkDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 链接id
     */
    private Integer navId;
    /**
     * 链接名称
     */
    @NotBlank(message = "navName为必传项")
    @ParamLengthValid(max = 10, message = "navName最长10个字符")
    private String navName;

    /**
     * 链接/路由地址
     */
    @NotBlank(message = "navLink为必传项")
    @ParamLengthValid(max = 1024, message = "navLink最长10个字符")
    private String navLink;

    /**
     * 链接类型, 1、路由 2、链接
     */
    @NotNull(message = "navType为必传项")
    @ParamRangeValid(ranges = {"1", "2"}, message = "范围不允许,navType范围应在{1,2}之间")
    private Integer navType;

    @ParamRangeValid(ranges = {"0", "1"}, message = "范围不允许,navMustLogin范围应在{0,1}之间")
    private Integer navMustLogin;

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
}
