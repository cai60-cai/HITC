package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.notice.GetNoticeListDTO;
import com.cxs.dto.admin.notice.SaveOrUpdateNoticeDTO;
import com.cxs.model.SystemInfo;
import com.cxs.service.NoticeInfoService;
import com.cxs.service.SystemInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/system-info")
@Api(tags = "管理员关于本站控制器")
public class AdminAboutSystemController {

    @Autowired
    private SystemInfoService systemInfoService;

    @PostMapping("/getSystemInfoInfo")
    @ApiOperation("管理员获取本站详情处理器")
    public BaseResult getSystemInfoInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        systemInfoService.getSystemInfoInfo(request, result);
        return result;
    }

    @PostMapping("/updateSystemInfo")
    @ApiOperation("管理员修改本站信息处理器")
    @MarkLog(desc = "管理员修改关于本站信息")
    public BaseResult updateSystemInfo(@RequestBody SystemInfo info, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        systemInfoService.updateSystemInfo(info, request, result);
        return result;
    }
}
