package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.log.GetLogListDTO;
import com.cxs.dto.admin.log.GetLogListReqDTO;
import com.cxs.dto.admin.task.GetTaskLogListDTO;
import com.cxs.dto.admin.task.GetTaskLogListReqDTO;
import com.cxs.dto.admin.task.SaveOrUpdateTaskDTO;
import com.cxs.dto.admin.task.TaskHandleDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ScheduledService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Api(tags = "管理员定时任务控制器")
@RequestMapping("/admin/task")
public class AdminTaskJobController {

    @Autowired
    private ScheduledService scheduledService;

    @PostMapping("/getTaskList")
    @ApiOperation("获取任务列表处理器")
    @PreAuthorize("hasAnyRole('super_admin','admin')")
    public BaseResult getTaskList(String keyword, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        scheduledService.getTaskList(keyword, request, result);
        return result;
    }

    @PostMapping("/saveOrUpdateTask")
    @ApiOperation("新增或修改任务处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "新增或修改任务")
    public BaseResult saveOrUpdateTask(@RequestBody @Validated SaveOrUpdateTaskDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        scheduledService.saveOrUpdateTask(dto, request, result);
        return result;
    }

    @DeleteMapping("/removeTask/{taskId}")
    @ApiOperation("删除任务处理器")
    @PreAuthorize("hasRole('super_admin')")
    @MarkLog(desc = "删除任务")
    public BaseResult removeTask(@PathVariable("taskId") Integer taskId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        scheduledService.removeTask(taskId, request, result);
        return result;
    }

    @PostMapping("/taskOperaHandle")
    @ApiOperation("定时任务操作处理器")
    @PreAuthorize("hasAnyRole('super_admin','admin')")
    @MarkLog(desc = "定时任务操作")
    public BaseResult taskOperaHandle(@RequestBody @Validated TaskHandleDTO dto,  HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        scheduledService.taskOperaHandle(dto, request, result);
        return result;
    }

    @GetMapping("/executeTask/{taskId}")
    @ApiOperation("执行任务处理器")
    @PreAuthorize("hasAnyRole('super_admin','admin')")
    @MarkLog(desc = "执行任务")
    public BaseResult executeTask(@PathVariable("taskId") Integer taskId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        scheduledService.executeOnce(taskId, request, result);
        return result;
    }

    @PostMapping("/getTaskLog")
    @ApiOperation("获取定时任务日志")
    @PreAuthorize("hasAnyRole('super_admin','admin')")
    public BaseResult getTaskLog(@RequestBody @Validated GetTaskLogListReqDTO req, HttpServletRequest request){
        GetTaskLogListDTO dto = new GetTaskLogListDTO();
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
        scheduledService.getTaskLog(dto, request, result);
        return result;
    }
}
