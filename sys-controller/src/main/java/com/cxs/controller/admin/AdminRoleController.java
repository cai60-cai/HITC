package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.KeyWordSearchDTO;
import com.cxs.dto.admin.role.AuthRoleToUserDTO;
import com.cxs.dto.admin.role.DeleteRolesDTO;
import com.cxs.dto.admin.role.SaveOrUpdateRoleDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.RoleService;
import com.cxs.vo.admin.menu.AdminMenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/admin/role")
@Api(tags = "管理员角色控制器")
public class AdminRoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/saveOrUpdateRole")
    @ApiOperation("新增/修改角色处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "新增/修改角色")
    public BaseResult saveOrUpdateRole(@RequestBody @Validated SaveOrUpdateRoleDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!saveOrUpdateRoleParamHandle(dto, result)) {
            return result;
        }
        Integer roleId = dto.getRoleId();
        List<Integer> menusId = new ArrayList<>();
        getMenusIdFromTree(dto.getRoutes(), menusId);
        if (ObjectUtils.isEmpty(roleId)) {
            roleService.saveRole(dto, menusId, request, result);
        } else {
            roleService.updateRole(dto, menusId, request, result);
        }
        return result;
    }

    /**
     * 根据树形获取ids
     * @param routes
     * @return
     */
    private void getMenusIdFromTree(List<AdminMenuVO> routes, List<Integer> list) {
        if (CollectionUtils.isEmpty(routes)) {
            return;
        } else {
            for (AdminMenuVO route : routes) {
                list.add(route.getMenuId());
                if (!CollectionUtils.isEmpty(route.getChildren())) {
                    getMenusIdFromTree(route.getChildren(), list);
                }
            }
        }
    }

    @PostMapping("/deleteRoles")
    @ApiOperation("删除角色处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "删除角色")
    public BaseResult deleteRoles(@RequestBody DeleteRolesDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        roleService.deleteRoles(dto, request, result);
        return result;
    }

    @PostMapping("/getRoleList")
    @ApiOperation("获取角色列表处理器")
    public BaseResult getRoleList(@RequestBody KeyWordSearchDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        roleService.getRoleList(dto, request, result);
        return result;
    }

    @PostMapping("/authRoleToUser")
    @ApiOperation("给用户授权处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "给用户授权")
    public BaseResult authRoleToUser(@RequestBody @Validated AuthRoleToUserDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        roleService.authRoleToUser(dto, request, result);
        return result;
    }

    @GetMapping("/getRoleListByUserId")
    @ApiOperation("根据用户id获取已有角色处理器")
    @PreAuthorize("hasRole('super_admin')")
    public BaseResult getRoleListByUserId(@RequestParam("userId") String userId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!StringUtils.hasLength(userId)) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode())
                    .setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",用户id为必传项");
            return result;
        }
        roleService.getRoleListByUserId(userId, request, result);
        return result;
    }

    @GetMapping("/getRoleListAndTreeMenu")
    @ApiOperation("获取角色列表和属性菜单处理器")
    public BaseResult getRoleListAndTreeMenu(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        roleService.getRoleListAndTreeMenu(request, result);
        return result;
    }

    /**
     * saveOrUpdateRole参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean saveOrUpdateRoleParamHandle(SaveOrUpdateRoleDTO dto, BaseResult result) {
        String pattern = "[\\u4e00-\\u9fa5]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(dto.getRoleName());
        if (m.find()) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",roleName不可传入中文");
            return false;
        }
        return true;
    }
}
