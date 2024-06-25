package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.dto.report.AddReportDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ReportService;
import com.cxs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/report")
@Api(tags = "用户举报控制器")
public class ReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @PostMapping("/addReport")
    @ApiOperation("用户新增举报处理器")
    public BaseResult addReport(@RequestBody @Validated AddReportDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!addReportParamCheck(dto, result)) {
            return result;
        }
        reportService.addReport(dto, request, result);
        return result;
    }

    @DeleteMapping("/delReport/{id}")
    @ApiOperation("用户删除举报处理器")
    public BaseResult delReport(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        reportService.delReport(id, request, result);
        return result;
    }

    /**
     * 用户添加反馈参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean addReportParamCheck(AddReportDTO dto, BaseResult result) {
        if (!CollectionUtils.isEmpty(dto.getReportImages()) && dto.getReportImages().size() > 3) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("举报图片不得超过三张");
            return false;
        }
        return true;
    }
}
