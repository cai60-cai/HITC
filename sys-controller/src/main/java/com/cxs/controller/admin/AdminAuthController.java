package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/auth")
@Api(tags = "管理员用户认证信息控制器")
public class AdminAuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/getUserInfo")
    @ApiOperation("获取管理员用户信息处理器")
    public BaseResult getUserInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.getUserInfo(request, result);
        return result;
    }
}
