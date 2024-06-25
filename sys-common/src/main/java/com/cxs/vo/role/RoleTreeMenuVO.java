package com.cxs.vo.role;

import com.cxs.vo.admin.menu.AdminMenuVO;
import lombok.Data;

import java.util.List;


@Data
public class RoleTreeMenuVO extends RoleVO{
    private List<AdminMenuVO> routes;
}
