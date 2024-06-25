package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.feedback.GetFeedBackListDTO;
import com.cxs.dto.admin.feedback.HandleFeedBackDTO;
import com.cxs.dto.feedback.AddFeedbackDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.FeedbackService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/feedback")
@Api(tags = "管理员反馈控制器")
public class AdminFeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/getFeedBackList")
    @ApiOperation("管理员获取用户反馈处理器")
    public BaseResult getFeedBackList(@RequestBody @Validated GetFeedBackListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getFeedBackListParamCheck(dto, result)) {
            return result;
        }
        feedbackService.getFeedBackList(dto, request, result);
        return result;
    }

    @PostMapping("/handleFeedBack")
    @ApiOperation("管理员处理用户反馈处理器")
    @MarkLog(desc = "管理员处理用户反馈")
    public BaseResult handleFeedBack(@RequestBody @Validated HandleFeedBackDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        feedbackService.handleFeedBack(dto, request, result);
        return result;
    }

    @DeleteMapping("/removeFeedBack/{id}")
    @ApiOperation("管理员删除用户反馈处理器")
    @MarkLog(desc = "管理员删除用户反馈")
    public BaseResult removeFeedBack(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        feedbackService.adminDelFeedback(id, request, result);
        return result;
    }

    @GetMapping("/adminGetFeedBackInfo/{id}")
    @ApiOperation("管理员获取用户反馈详情处理器")
    @MarkLog(desc = "管理员获取用户反馈详情")
    public BaseResult adminGetFeedBackInfo(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        feedbackService.adminGetFeedBackInfo(id, request, result);
        return result;
    }

    /**
     * 管理员获取用户反馈参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean getFeedBackListParamCheck(GetFeedBackListDTO dto, BaseResult result) {
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
}
