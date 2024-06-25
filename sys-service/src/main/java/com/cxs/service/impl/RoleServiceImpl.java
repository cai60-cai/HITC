package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.config.SystemInnerConfig;
import com.cxs.dto.KeyWordSearchDTO;
import com.cxs.dto.admin.role.AuthRoleToUserDTO;
import com.cxs.dto.admin.role.DeleteRolesDTO;
import com.cxs.dto.admin.role.SaveOrUpdateRoleDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.RoleMenuMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.mapper.UserRoleMapper;
import com.cxs.model.Role;
import com.cxs.model.RoleMenu;
import com.cxs.model.User;
import com.cxs.model.UserRole;
import com.cxs.service.MenuService;
import com.cxs.service.RoleMenuService;
import com.cxs.service.RoleService;
import com.cxs.mapper.RoleMapper;
import com.cxs.service.UserRoleService;
import com.cxs.service.UserService;
import com.cxs.vo.role.RoleTreeMenuVO;
import com.cxs.vo.role.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SystemInnerConfig systemInnerConfig;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    @Transactional
    public void saveRole(SaveOrUpdateRoleDTO dto, List<Integer> menusId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Role role = new Role();
            BeanUtils.copyProperties(dto, role);
            int insert = 0;
            try {
                insert = roleMapper.insert(role);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",角色名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增角色失败");
            } else {
                List<RoleMenu> roleMenuList = new ArrayList<>();
                for (Integer menuId : menusId) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(role.getRoleId());
                    roleMenu.setMenuId(menuId);
                    roleMenuList.add(roleMenu);
                }
                if (!roleMenuService.saveBatch(roleMenuList)) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增角色失败");
                }
                result.setData(role);
            }
        } catch (Exception e) {
            log.error("新增角色失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增、修改角色接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    @Transactional
    public void updateRole(SaveOrUpdateRoleDTO dto, List<Integer> menusId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Role roleInfo = roleMapper.selectById(dto.getRoleId());
            if (ObjectUtils.isEmpty(roleInfo)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该角色不存在");
                return;
            }
            if (systemInnerConfig.getInnerRoleList().contains(dto.getRoleId())) {
                dto.setRoleName(null);
            }
            Role role = new Role();
            BeanUtils.copyProperties(dto, role);
            int insert = 0;
            try {
                insert = roleMapper.updateById(role);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",角色名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改角色失败");
            } else {
                QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
                wrapper.eq("role_id", role.getRoleId());
                roleMenuMapper.delete(wrapper);
                List<RoleMenu> roleMenuList = new ArrayList<>();
                for (Integer menuId : menusId) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(role.getRoleId());
                    roleMenu.setMenuId(menuId);
                    roleMenuList.add(roleMenu);
                }
                if (!roleMenuService.saveBatch(roleMenuList)) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改角色失败");
                }
            }
        } catch (Exception e) {
            log.error("修改角色失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增、修改角色接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void deleteRoles(DeleteRolesDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String msg = "";
            List<Integer> ids = dto.getIds();
            for (Integer innerId : systemInnerConfig.getInnerRoleList()) {
                if (ids.contains(innerId)) {
                    msg = "内置角色不可被删除";
                    ids.remove(innerId);
                    break;
                }
            }
            if (!CollectionUtils.isEmpty(ids)) {
                roleMapper.deleteBatchIds(ids);
                QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
                wrapper.in("role_id", ids);
                roleMenuMapper.delete(wrapper);
            } else {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + "," + msg);
            }
        } catch (Exception e) {
            log.error("删除角色失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【删除角色接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getRoleList(KeyWordSearchDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String keyWord = dto.getKeyWord();
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            if (StringUtils.hasLength(keyWord)) {
                wrapper.like("role_name", keyWord)
                        .or().like("role_desc", keyWord)
                        .or().like("role_id", keyWord);
            }
            List<Role> roleList = roleMapper.selectList(wrapper);
            List<RoleVO> voList = CollectionUtils.isEmpty(roleList) ? new ArrayList<>():
                    roleList.stream().map(r -> {
                        RoleVO vo = new RoleVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("查询角色列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【查询角色列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    @Transactional
    public void authRoleToUser(AuthRoleToUserDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            User user = userMapper.selectById(dto.getUserId());
            if (ObjectUtils.isEmpty(user)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该用户不存在");
            } else {
                UserSubject tokenUser = userService.getUserByToken(request);
                if (tokenUser.getId().equals(dto.getUserId())) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",禁止自己给自己授权");
                    return;
                }
                QueryWrapper<UserRole> deleteWrapper = new QueryWrapper<>();
                deleteWrapper.eq("user_id", dto.getUserId());
                userRoleMapper.delete(deleteWrapper);
                if (CollectionUtils.isEmpty(dto.getRoleIds())) {
                    dto.getRoleIds().add(systemInnerConfig.getUserRoleId());
                }
                List<UserRole> userRoleList = dto.getRoleIds().stream().map(r -> {
                    UserRole role = new UserRole();
                    role.setUserId(dto.getUserId());
                    role.setRoleId(r);
                    return role;
                }).collect(Collectors.toList());
                this.userRoleService.saveBatch(userRoleList);
            }
        } catch (Exception e) {
            log.error("给用户授权失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【给用户授权接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getRoleListByUserId(String userId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<Role> roleList = roleMapper.selectRoleByUserId(userId.trim());
            List<RoleVO> roleVOList = CollectionUtils.isEmpty(roleList) ? new ArrayList<>(0) :
                    roleList.stream().map(r -> {
                        RoleVO vo = new RoleVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(roleVOList);
        } catch (Exception e) {
            log.error("获取用户id获取角色列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户id获取角色列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getRoleListAndTreeMenu(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            List<Role> roleList = roleMapper.selectList(wrapper);
            List<RoleTreeMenuVO> voList = CollectionUtils.isEmpty(roleList) ? new ArrayList<>():
                    roleList.stream().map(r -> {
                        RoleTreeMenuVO vo = new RoleTreeMenuVO();
                        BeanUtils.copyProperties(r, vo);
                        vo.setRoutes(menuService.getTreeMenuByRoleId(r.getRoleId()));
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("查询角色列表及树形菜单失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【查询角色列表及树形菜单接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }
}




