package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.report.AdminGetReportListDTO;
import com.cxs.dto.admin.report.HandleReportDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/report")
@Api(tags = "管理员举报控制器")
public class AdminReportController {

    @Autowired
    private ReportService reportService;


    @PostMapping("/adminGetReportList")
    @ApiOperation("管理员获取用户举报处理器")
    public BaseResult adminGetReportList(@RequestBody @Validated AdminGetReportListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!adminGetReportListParamCheck(dto, result)) {
            return result;
        }
        reportService.adminGetReportList(dto, request, result);
        return result;
    }

    private boolean adminGetReportListParamCheck(AdminGetReportListDTO dto, BaseResult result) {
        if (dto.getPageNum() <= 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("pageNum传递错误");
            return false;
        }
        if (dto.getPageSize() < 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("pageSize传递错误");
            return false;
        }
        return true;
    }

    @GetMapping("/adminGetHandleType")
    @ApiOperation("管理员获取举报处理方式处理器")
    public BaseResult adminGetHandleType(@RequestParam("handleType") Integer handleType, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        reportService.adminGetHandleType(handleType, request, result);
        return result;
    }

    @PostMapping("/adminHandleReport")
    @ApiOperation("管理员处理用户举报处理器")
    @MarkLog(desc = "管理员处理用户举报")
    public BaseResult adminHandleReport(@RequestBody @Validated HandleReportDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        reportService.adminHandleReport(dto, request, result);
        return result;
    }

    @DeleteMapping("/removeReport/{id}")
    @ApiOperation("管理员删除用户举报处理器")
    @MarkLog(desc = "管理员删除用户举报")
    public BaseResult removeReport(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        reportService.adminRemoveReport(id, request, result);
        return result;
    }

    @GetMapping("/adminGetReportInfo/{id}")
    @ApiOperation("管理员获取用户举报详情处理器")
    @MarkLog(desc = "管理员获取用户举报详情")
    public BaseResult adminGetReportInfo(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        reportService.adminGetReportInfo(id, request, result);
        return result;
    }


}
