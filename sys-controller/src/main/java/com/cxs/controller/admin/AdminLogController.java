package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.log.GetLogListDTO;
import com.cxs.dto.admin.log.GetLogListReqDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.LogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/log")
@Api(tags = "管理员日志控制器")
public class AdminLogController {

    @Autowired
    private LogInfoService logInfoService;

    @PostMapping("/getLogList")
    @ApiOperation("查询日志处理器")
    public BaseResult getLogList(@RequestBody @Validated GetLogListReqDTO req, HttpServletRequest request){
        GetLogListDTO dto = new GetLogListDTO();
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(req.getPageNum()) || req.getPageNum() <= 0) {
            req.setPageNum(1);
        }
        if (ObjectUtils.isEmpty(req.getPageSize()) || req.getPageSize() < 0) {
            req.setPageSize(10);
        }
        BeanUtils.copyProperties(req, dto);
        if (!CollectionUtils.isEmpty(req.getTimeRange())) {
            if (req.getTimeRange().size() != 2) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg("时间范围有误");
                return result;
            } else {
                List<LocalDateTime> timeRange = req.getTimeRange();
                dto.setStartTime(timeRange.get(0));
                dto.setEndTime(timeRange.get(1));
            }
        }
        logInfoService.getLogList(dto, request, result);
        return result;
    }
}
