package com.cxs.dto.admin.menu;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Data
public class AuthMenuToRoleDTO {

    @NotNull(message = "用户id为必传项")
    private Integer roleId;

    @NotNull(message = "菜单id集合为必传项")
    private Set<Integer> menuIds;
}
