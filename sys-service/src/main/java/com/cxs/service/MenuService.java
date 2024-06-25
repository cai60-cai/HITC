package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.menu.AuthMenuToRoleDTO;
import com.cxs.dto.admin.menu.DeleteMenusDTO;
import com.cxs.dto.admin.menu.SaveOrUpdateMenuDTO;
import com.cxs.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxs.vo.admin.menu.AdminMenuVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface MenuService extends IService<Menu> {
    /**
     * 根据用户id获取树形菜单
     * @param userId
     * @return
     */
    List<AdminMenuVO> getAdminMenuListByUserId(String userId, Boolean all);

    /**
     * 新增菜单
     * @param dto
     * @param request
     * @param result
     */
    void saveMenu(SaveOrUpdateMenuDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改菜单
     * @param dto
     * @param request
     * @param result
     */
    void updateMenu(SaveOrUpdateMenuDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除菜单
     * @param dto
     * @param request
     * @param result
     */
    void deleteMenus(DeleteMenusDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取所有菜单，树形
     * @param request
     * @param result
     */
    void getAllTreeMenu(HttpServletRequest request, BaseResult result);

    /**
     * 给角色授权
     * @param dto
     * @param request
     * @param result
     */
    void authMenuToRole(AuthMenuToRoleDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户已有权限，树形
     * @param userId
     * @param request
     * @param result
     */
    void getTreeMenuByUserId(String userId, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户已有菜单
     * @param request
     * @param result
     */
    void getTreeMenuByToken(HttpServletRequest request, BaseResult result);

    /**
     * 根据角色获取菜单，树形
     * @param roleId
     */
    List<AdminMenuVO> getTreeMenuByRoleId(Integer roleId);
}
