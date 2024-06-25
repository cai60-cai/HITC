package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.config.SystemInnerConfig;
import com.cxs.dto.admin.menu.AuthMenuToRoleDTO;
import com.cxs.dto.admin.menu.DeleteMenusDTO;
import com.cxs.dto.admin.menu.SaveOrUpdateMenuDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.RoleMapper;
import com.cxs.mapper.RoleMenuMapper;
import com.cxs.mapper.UserRoleMapper;
import com.cxs.model.Menu;
import com.cxs.model.Role;
import com.cxs.model.RoleMenu;
import com.cxs.model.UserRole;
import com.cxs.service.MenuService;
import com.cxs.mapper.MenuMapper;
import com.cxs.service.RoleMenuService;
import com.cxs.service.UserService;
import com.cxs.vo.admin.menu.AdminMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService{

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private SystemInnerConfig systemInnerConfig;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserService userService;

    @Override
    public List<AdminMenuVO> getAdminMenuListByUserId(String userId, Boolean all) {
        List<AdminMenuVO> list = new ArrayList<>();
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        if (!all) {
            List<Integer> menuIds = menuMapper.getAllMenuIdByUserId(userId);
            if (CollectionUtils.isEmpty(menuIds)) {
                return list;
            }
            wrapper.in("menu_id", menuIds);
        }
        List<Menu> menuList = menuMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(menuList)) {
            return list;
        }
        List<AdminMenuVO> parentNode = menuList.stream()
                .filter(m -> m.getMenuParantId().equals(-1))
                .map(m -> menuToAdminMenuVOHandle(m))
                .collect(Collectors.toList());
        list.addAll(parentNode);

        for (AdminMenuVO menuVO : list) {
            List<AdminMenuVO> children = getMenuChild(menuVO.getMenuId(), menuList);
            menuVO.setChildren(children);
        }

        return list;
    }

    @Override
    public void saveMenu(SaveOrUpdateMenuDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Menu menu = menuDtoToMenu(dto);
            if (!menu.getMenuParantId().equals(-1)) {
                Menu parantMenu = menuMapper.selectById(menu.getMenuParantId());
                if (ObjectUtils.isEmpty(parantMenu)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",父级菜单不存在");
                    return;
                }
            }
            int insert = 0;
            try {
                insert = menuMapper.insert(menu);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",菜单名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增权限菜单失败");
            }
        } catch (Exception e) {
            log.error("新增权限菜单失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增、修改权限菜单接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateMenu(SaveOrUpdateMenuDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Menu menu = menuDtoToMenu(dto);
            if (!menu.getMenuParantId().equals(-1)) {
                Menu parantMenu = menuMapper.selectById(menu.getMenuParantId());
                if (ObjectUtils.isEmpty(parantMenu)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",父级菜单不存在");
                    return;
                }
            }
            int insert = 0;
            try {
                insert = menuMapper.updateById(menu);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",菜单名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改权限菜单失败");
            }
        } catch (Exception e) {
            log.error("修改权限菜单失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增、修改权限菜单接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    @Transactional
    public void deleteMenus(DeleteMenusDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String msg = "";
            List<Integer> ids = dto.getMenuIds();
            if (!CollectionUtils.isEmpty(ids)) {
                menuMapper.deleteBatchIds(ids);
                QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
                wrapper.in("menu_id", ids);
                roleMenuMapper.delete(wrapper);
            } else {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + "," + msg);
            }
        } catch (Exception e) {
            log.error("删除菜单失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【删除菜单接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getAllTreeMenu(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<AdminMenuVO> list = this.getAdminMenuListByUserId(null, Boolean.TRUE);
            result.setData(list);
        } catch (Exception e) {
            log.error("获取树形权限菜单失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取树形权限菜单接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void authMenuToRole(AuthMenuToRoleDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Integer roleId = dto.getRoleId();
            Role role = roleMapper.selectById(roleId);
            if (ObjectUtils.isEmpty(role)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",角色不存在");
            } else {
                Set<Integer> menuIds = dto.getMenuIds();
                menuIds.addAll(systemInnerConfig.getInnerMenuList());
                // 删除权限
                QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
                wrapper.eq("role_id", roleId);
                roleMenuMapper.delete(wrapper);
                List<RoleMenu> menuList = new ArrayList<>();
                for (Integer menuId : menuIds) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    menuList.add(roleMenu);
                }
                this.roleMenuService.saveBatch(menuList);
            }
        } catch (Exception e) {
            log.error("给角色授权失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【给角色授权接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, "", result);
        }
    }

    @Override
    public void getTreeMenuByUserId(String userId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<AdminMenuVO> list = null;
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId.trim());
            List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);
            List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            if (roleIds.contains(systemInnerConfig.getSuperAdminRoleId())) {
                list = this.getAdminMenuListByUserId(userId, Boolean.TRUE);
            } else {
                list = this.getAdminMenuListByUserId(userId, Boolean.FALSE);
            }
            result.setData(list);
        } catch (Exception e) {
            log.error("获取用户id获取树形权限失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户id获取树形权限接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getTreeMenuByToken(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<AdminMenuVO> list = null;
            UserSubject token = this.userService.getUserByToken(request);
            String userId = token.getId();
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId.trim());
            List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);
            List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            if (roleIds.contains(systemInnerConfig.getSuperAdminRoleId())) {
                list = this.getAdminMenuListByUserId(userId, Boolean.TRUE);
            } else {
                list = this.getAdminMenuListByUserId(userId, Boolean.FALSE);
            }
            result.setData(list);
        } catch (Exception e) {
            log.error("根据用户token获取树形权限失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【根据用户token获取树形权限接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public List<AdminMenuVO> getTreeMenuByRoleId(Integer roleId) {
        List<AdminMenuVO> list = new ArrayList<>();
        if (ObjectUtils.isEmpty(roleId)) {
            return list;
        }
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(wrapper);
        List<Integer> menuIds = roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        if (roleId.equals(systemInnerConfig.getSuperAdminRoleId())) {
            menuIds = menuMapper.selectList(null).stream().map(Menu::getMenuId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(menuIds)) {
            return list;
        } else {
            QueryWrapper<Menu> menuWrapper = new QueryWrapper<>();
            menuWrapper.in("menu_id", menuIds);
            List<Menu> menuList = menuMapper.selectList(menuWrapper);
            List<AdminMenuVO> parentNode = menuList.stream()
                    .filter(m -> m.getMenuParantId().equals(-1))
                    .map(m -> menuToAdminMenuVOHandle(m))
                    .collect(Collectors.toList());
            list.addAll(parentNode);

            for (AdminMenuVO menuVO : list) {
                List<AdminMenuVO> children = getMenuChild(menuVO.getMenuId(), menuList);
                menuVO.setChildren(children);
            }
        }
        return list;
    }

    /**
     * 递归查找所有孩子节点
     * @param menuId
     * @param menuList
     * @return
     */
    private List<AdminMenuVO> getMenuChild(Integer menuId, List<Menu> menuList) {
        List<AdminMenuVO> children = menuList.stream().filter(m -> m.getMenuParantId().equals(menuId))
                .map(m -> menuToAdminMenuVOHandle(m))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(children)) {
            return new ArrayList<>();
        }
        for (AdminMenuVO child : children) {
            List<AdminMenuVO> menuChild = getMenuChild(child.getMenuId(), menuList);
            child.setChildren(menuChild);
        }
        return children;
    }

    /**
     * menu  --> AdminMenuVO
     * @param menu
     * @return
     */
    private AdminMenuVO menuToAdminMenuVOHandle(Menu menu) {
        if (ObjectUtils.isEmpty(menu)) {
            return null;
        }
        return AdminMenuVO.builder()
                .menuId(menu.getMenuId())
                .name(menu.getMenuName())
                .path(menu.getMenuPath())
                .component(menu.getMenuComponent())
                .redirect(menu.getMenuRedirect())
                .parentId(menu.getMenuParantId())
                .hidden(menu.getMenuHidden() == 1)
                .alwaysShow(menu.getMenuAlwaysShow() == 1)
                .meta(ObjectUtils.isEmpty(JSON.parseObject(menu.getMenuMeta())) ? new JSONObject() : JSON.parseObject(menu.getMenuMeta()))
                .build();
    }

    private Menu menuDtoToMenu(SaveOrUpdateMenuDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        }
        Menu menu = new Menu();
        menu.setMenuId(dto.getMenuId());
        menu.setMenuName(dto.getName());
        menu.setMenuPath(dto.getPath());
        menu.setMenuComponent(dto.getComponent());
        menu.setMenuRedirect(dto.getRedirect());
        menu.setMenuHidden(dto.getHidden());
        menu.setMenuAlwaysShow(dto.getAlwaysShow());
        menu.setMenuMeta(dto.getMeta());
        menu.setMenuParantId(dto.getMenuParantId());
        return menu;
    }
}




