package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.service.SystemInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/dashboard")
@Api(tags = "管理员首页看板控制器")
public class AdminDashBoardController {


    @Autowired
    private SystemInfoService systemInfoService;


    @PostMapping("/getSystemStatisticsInfo")
    @ApiOperation("管理员获取看板统计处理器")
    public BaseResult getSystemStatisticsInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        systemInfoService.getSystemStatisticsInfo(request, result);
        return result;
    }

    @PostMapping("/getSystemRangeStatisticsInfo")
    @ApiOperation("管理员获取看板范围统计处理器")
    public BaseResult getSystemRangeStatisticsInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        systemInfoService.getSystemRangeStatisticsInfo(request, result);
        return result;
    }
}
