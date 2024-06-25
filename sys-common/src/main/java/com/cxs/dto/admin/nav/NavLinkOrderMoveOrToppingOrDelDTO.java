package com.cxs.dto.admin.nav;

import com.cxs.valid.annotation.ParamRangeValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("导航链接操作dto")
public class NavLinkOrderMoveOrToppingOrDelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "navId为必传项")
    @ApiModelProperty("导航链接id")
    private Integer navId;

    @NotNull(message = "operaType为必传项")
    @ParamRangeValid(ranges = {"1", "2", "3", "4"}, message = "operaType取值范围为{1, 2, 3, 4}")
    @ApiModelProperty("导航链接操作类型,1、置顶2、上移3、下移4、删除")
    private Integer operaType;
}
