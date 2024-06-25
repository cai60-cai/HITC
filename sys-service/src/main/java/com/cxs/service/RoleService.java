package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.KeyWordSearchDTO;
import com.cxs.dto.admin.role.AuthRoleToUserDTO;
import com.cxs.dto.admin.role.DeleteRolesDTO;
import com.cxs.dto.admin.role.SaveOrUpdateRoleDTO;
import com.cxs.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface RoleService extends IService<Role> {
    /**
     * 创建角色
     * @param dto
     * @param request
     * @param result
     */
    void saveRole(SaveOrUpdateRoleDTO dto, List<Integer> menusId, HttpServletRequest request, BaseResult result);

    /**
     * 修改角色
     * @param dto
     * @param request
     * @param result
     */
    void updateRole(SaveOrUpdateRoleDTO dto, List<Integer> menusId, HttpServletRequest request, BaseResult result);

    /**
     * 删除角色
     * @param dto
     * @param request
     * @param result
     */
    void deleteRoles(DeleteRolesDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取角色列表
     * @param dto
     * @param request
     * @param result
     */
    void getRoleList(KeyWordSearchDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 给用户授权
     * @param dto
     * @param request
     * @param result
     */
    void authRoleToUser(AuthRoleToUserDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 根据用户id获取已有角色
     * @param userId
     * @param request
     * @param result
     */
    void getRoleListByUserId(String userId, HttpServletRequest request, BaseResult result);

    /**
     * 获取角色列表和树形菜单
     * @param request
     * @param result
     */
    void getRoleListAndTreeMenu(HttpServletRequest request, BaseResult result);
}
