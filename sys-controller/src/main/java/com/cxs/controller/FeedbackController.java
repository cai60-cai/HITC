package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.dto.feedback.AddFeedbackDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.FeedbackService;
import com.cxs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/feedback")
@Api(tags = "用户反馈控制器")
public class FeedbackController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/addFeedback")
    @ApiOperation("用户新增反馈处理器")
    public BaseResult addFeedback(@RequestBody @Validated AddFeedbackDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!addFeedbackParamCheck(dto, result)) {
            return result;
        }
        feedbackService.addFeedback(dto, request, result);
        return result;
    }

    @DeleteMapping("/delFeedback/{id}")
    @ApiOperation("用户删除反馈处理器")
    public BaseResult delFeedback(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        feedbackService.delFeedback(id, request, result);
        return result;
    }

    /**
     * 用户添加反馈参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean addFeedbackParamCheck(AddFeedbackDTO dto, BaseResult result) {
        String feedbackEmail = dto.getFeedbackEmail();
        if (StringUtils.hasLength(feedbackEmail)) {
            String emailPattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
            Pattern emailR = Pattern.compile(emailPattern);
            Matcher emailM = emailR.matcher(feedbackEmail);
            if (!emailM.matches()) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("邮箱格式错误");
                return false;
            }
        }
        if (!CollectionUtils.isEmpty(dto.getFeedbackImages()) && dto.getFeedbackImages().size() > 3) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("反馈图片不得超过三张");
            return false;
        }
        return true;
    }
}
