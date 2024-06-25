package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.menu.AuthMenuToRoleDTO;
import com.cxs.dto.admin.menu.DeleteMenusDTO;
import com.cxs.dto.admin.menu.SaveOrUpdateMenuDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/admin/menu")
@Api(tags = "管理员权限控制器")
public class AdminMenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/saveOrUpdateRole")
    @ApiOperation("新增/修改权限处理器")
    @PreAuthorize("hasRole('super_admin')")
    public BaseResult saveOrUpdateMenu(@RequestBody @Validated SaveOrUpdateMenuDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!saveOrUpdateMenuParamHandle(dto, result)) {
            return result;
        }
        Integer menuId = dto.getMenuId();
        if (ObjectUtils.isEmpty(menuId)) {
            menuService.saveMenu(dto, request, result);
        } else {
            menuService.updateMenu(dto, request, result);
        }
        return result;
    }

    @PostMapping("/deleteMenus")
    @ApiOperation("删除权限处理器")
    @PreAuthorize("hasRole('super_admin')")
    public BaseResult deleteMenus(@RequestBody DeleteMenusDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        menuService.deleteMenus(dto, request, result);
        return result;
    }

    @GetMapping("/getAllTreeMenu")
    @ApiOperation("获取树形权限处理器")
    @PreAuthorize("hasRole('super_admin')")
    public BaseResult getAllTreeMenu(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        menuService.getAllTreeMenu(request, result);
        return result;
    }

    @PostMapping("/authMenuToRole")
    @ApiOperation("给角色授权处理器")
    @PreAuthorize("hasRole('super_admin')")
    public BaseResult authMenuToRole(@RequestBody @Validated AuthMenuToRoleDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        menuService.authMenuToRole(dto, request, result);
        return result;
    }

    @GetMapping("/getTreeMenuByUserId")
    @ApiOperation("获取用户id获取树形权限处理器")
    public BaseResult getTreeMenuByUserId(@RequestParam("userId") String userId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!StringUtils.hasLength(userId)) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode())
                    .setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",用户id为必传项");
            return result;
        }
        menuService.getTreeMenuByUserId(userId, request, result);
        return result;
    }

    @GetMapping("/getTreeMenuByToken")
    @ApiOperation("获取用户Token获取树形权限处理器")
    public BaseResult getTreeMenuByToken(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        menuService.getTreeMenuByToken(request, result);
        return result;
    }


    /**
     * saveOrUpdateMenu参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean saveOrUpdateMenuParamHandle(SaveOrUpdateMenuDTO dto, BaseResult result) {
        Integer menuParantId = dto.getMenuParantId();
        if (menuParantId < -1 || menuParantId == 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",menuParantId传递有误");
            return false;
        }
        return true;
    }
}
