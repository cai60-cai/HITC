package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.dto.KeyWordSearchDTO;
import com.cxs.dto.admin.user.CreateUserDTO;
import com.cxs.dto.admin.user.GetSimpleUserListDTO;
import com.cxs.dto.admin.user.GetUserListDTO;
import com.cxs.dto.admin.user.UpdateStatusDTO;
import com.cxs.dto.admin.user.UpdateUserAuthDTO;
import com.cxs.dto.admin.user.UpdateUserInfoDTO;
import com.cxs.dto.admin.user.UpdateUserPwdDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/user")
@Api(tags = "管理员用户控制器")
public class AdminUserController {

    @Autowired
    private UserService userService;


    @PostMapping("/getUserList")
    @ApiOperation("获取用户列表处理器")
    public BaseResult getUserList(@RequestBody GetUserListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getUserListParamHandle(dto, result)) {
            return result;
        }
        userService.getUserList(dto, request, result);
        return result;
    }

    @PostMapping("/updateStatus")
    @ApiOperation("修改用户状态处理器")
    @MarkLog(desc = "修改用户状态")
    public BaseResult updateStatus(@RequestBody @Validated UpdateStatusDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.updateStatus(dto, request, result);
        return result;
    }

    @GetMapping("/resetUserPwd")
    @ApiOperation("重置用户密码处理器")
    @MarkLog(desc = "重置用户密码")
    public BaseResult resetUserPwd(@RequestParam("userId") String userId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.resetUserPwd(userId, request, result);
        return result;
    }

    @DeleteMapping("/deleteUser/{userId}")
    @ApiOperation("删除用户处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "删除用户")
    public BaseResult deleteUser(@PathVariable("userId") String userId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.deleteUser(userId, request, result);
        return result;
    }

    @PostMapping("/createUser")
    @ApiOperation("创建新用户处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "创建新用户")
    public BaseResult createUser(@RequestBody @Validated CreateUserDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.createUser(dto, request, result);
        return result;
    }

    @PostMapping("/checkUserPwd")
    @ApiOperation("检查用户密码处理器")
    public BaseResult checkUserPwd(@RequestParam("password") String password, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.checkUserPwd(password, request, result);
        return result;
    }

    @PostMapping("/updateUserPwd")
    @ApiOperation("修改管理员密码处理器")
    @MarkLog(desc = "修改管理员密码")
    public BaseResult updateUserPwd(@RequestBody @Validated UpdateUserPwdDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.updateUserPwd(dto, request, result);
        return result;
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户个人信息处理器")
    public BaseResult getUserInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.getUserDetailInfo(request, result);
        return result;
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation("修改管理员个人信息处理器")
    @MarkLog(desc = "修改管理员个人信息")
    public BaseResult updateUserInfo(@RequestBody @Validated UpdateUserInfoDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.updateUserInfo(dto, request, result);
        return result;
    }

    @GetMapping("/getUserAuth")
    @ApiOperation("获取用户权限处理器")
    public BaseResult getUserAuth(@RequestParam("userId") String userId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.getUserAuth(userId, request, result);
        return result;
    }

    @PostMapping("/updateUserAuth")
    @ApiOperation("修改用户权限处理器")
    @MarkLog(desc = "修改用户系统权限")
    public BaseResult updateUserAuth(@RequestBody @Validated UpdateUserAuthDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.updateUserAuth(dto, request, result);
        return result;
    }

    @PostMapping("/adminGetSimpleUserList")
    @ApiOperation("获取用户下拉列表处理器")
    @MarkLog(desc = "获取用户下拉列表")
    public BaseResult adminGetSimpleUserList(@RequestBody GetSimpleUserListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.adminGetSimpleUserList(dto, request, result);
        return result;
    }

    /**
     * 获取用户列表入参校验
     * @param dto
     * @param result
     * @return
     */
    private boolean getUserListParamHandle(GetUserListDTO dto, BaseResult result) {
        if (dto.getPageNum() < 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode())
                    .setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",pageNum不能为负值");
            return false;
        }
        if (dto.getPageSize() < 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode())
                    .setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",pageSize不能为负值");
            return false;
        }
        return true;
    }
}
