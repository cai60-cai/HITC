package com.cxs.controller;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.constant.CommonContent;
import com.cxs.dto.KeyWordSearchDTO;
import com.cxs.dto.KeyWordSearchPageDTO;
import com.cxs.dto.message.ReadMessageDTO;
import com.cxs.dto.profile.CheckOldPasswordDTO;
import com.cxs.dto.profile.CheckUsernameDTO;
import com.cxs.dto.profile.UserPointSettingDTO;
import com.cxs.dto.profile.UpdatePwdDTO;
import com.cxs.dto.profile.UpdateSelfInfoDTO;
import com.cxs.dto.profile.UserReceiveEmailNoticeSettingDTO;
import com.cxs.dto.profile.UserRewardSettingDTO;
import com.cxs.dto.profile.UserupdateRewardImgInfoDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.PointTradeFlow;
import com.cxs.model.UserReward;
import com.cxs.service.ArticleCommentService;
import com.cxs.service.PointTradeFlowService;
import com.cxs.service.SystemMessageService;
import com.cxs.service.UserService;
import com.cxs.utils.CheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
@Api(tags = "用户控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleCommentService articleCommentService;

    @Autowired
    private PointTradeFlowService pointTradeFlowService;

    @Autowired
    private SystemMessageService systemMessageService;

    @GetMapping("/logout")
    @ApiOperation("用户登出处理器")
    public BaseResult logout(HttpServletRequest request, HttpServletResponse response){
        BaseResult result = BaseResult.ok();
        userService.logout(request, response, result);
        return result;
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息处理器")
    public BaseResult getUserInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.getUserInfo(request, result);
        return result;
    }

    @GetMapping("/getLoginLog")
    @ApiOperation("获取用户登录日志处理器")
    public BaseResult getLoginLog(BaseRequest dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(dto)) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg());
            return result;
        }
        if (ObjectUtils.isEmpty(dto.getPageNum()) || dto.getPageNum() <= 0) dto.setPageNum(1);
        if (ObjectUtils.isEmpty(dto.getPageSize()) || dto.getPageSize() <= 0) dto.setPageSize(10);
        userService.getUserLoginLog(dto,request, result);
        return result;
    }

    @GetMapping("/getPersonal")
    @ApiOperation("获取用户个人资料处理器")
    public BaseResult getPersonal(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.getPersonal(request, result);
        return result;
    }

    /**
     * 查询用户名是否存在
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/checkUserNameExist")
    @ApiOperation("查询用户名是否存在处理器")
    public BaseResult checkUserNameExist(@RequestBody @Validated CheckUsernameDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.checkUserNameExist(dto, request, result);
        return result;
    }

    /**
     * 查询旧密码
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/checkOldPassword")
    @ApiOperation("查询旧密码处理器")
    public BaseResult checkOldPassword(@RequestBody @Validated CheckOldPasswordDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.checkOldPassword(dto, request, result);
        return result;
    }

    @PostMapping("/updatePassword")
    @ApiOperation("修改密码处理器")
    public BaseResult updatePassword(@RequestBody @Validated UpdatePwdDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.updatePassword(dto, request, result);
        return result;
    }

    /**
     * 更新个人信息
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/updateSelfInfo")
    @ApiOperation("修改个人用户信息处理器")
    public BaseResult updateSelfInfo(@RequestBody @Validated UpdateSelfInfoDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!updateSelfInfoParamCheck(dto, result)) {
            return result;
        }
        userService.updateSelfInfo(dto, request, result);
        return result;
    }

    /**
     * 获取用户个人设置
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getSettingInfo")
    @ApiOperation("获取用户个人设置处理器")
    public BaseResult getSettingInfo(HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.getSettingInfo(request, result);
        return result;
    }

    /**
     * 获取用户发布列表
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getArticlePublishList")
    @ApiOperation("获取用户发布列表处理器")
    public BaseResult getArticlePublishList(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!getArticlePublishListParamCheck(dto, result)) {
            return result;
        }
        userService.getArticlePublishList(dto, request, result);
        return result;
    }


    /**
     * 获取用户收藏列表
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getArticleCollList")
    @ApiOperation("获取用户收藏列表处理器")
    public BaseResult getArticleCollList(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!getArticleCollListParamCheck(dto, result)) {
            return result;
        }
        userService.getArticleCollList(dto, request, result);
        return result;
    }

    /**
     * 用户删除收藏列表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/removeArticleColl")
    @ApiOperation("用户删除收藏列表处理器")
    public BaseResult removeArticleColl(@RequestParam("id") Integer id, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.removeArticleColl(id, request, result);
        return result;
    }

    /**
     * 用户积分功能设置操作
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/operaPointSetting")
    @ApiOperation("用户积分功能设置操作处理器")
    public BaseResult operaPointSetting(@RequestBody @Validated UserPointSettingDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.operaPointSetting(dto, request, result);
        return result;
    }

    /**
     * 用户打赏功能设置操作
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/operaRewardSetting")
    @ApiOperation("用户打赏功能设置操作处理器")
    public BaseResult operaRewardSetting(@RequestBody @Validated UserRewardSettingDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.operaRewardSetting(dto, request, result);
        return result;
    }

    /**
     * 用户邮件接收设置操作
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/operaReceiveEmailNoticeSetting")
    @ApiOperation("用户邮件接收设置操作处理器")
    public BaseResult operaReceiveEmailNoticeSetting(@RequestBody @Validated UserReceiveEmailNoticeSettingDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.operaReceiveEmailNoticeSetting(dto, request, result);
        return result;
    }

    @PostMapping("/updateRewardImgInfo")
    @ApiOperation("更新用户打赏信息处理器")
    public BaseResult updateRewardImgInfo(@RequestBody UserupdateRewardImgInfoDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.updateRewardImgInfo(dto, request, result);
        return result;
    }

    @PostMapping("/searchUserResult")
    @ApiOperation("搜索用户处理器")
    public BaseResult searchUserResult(@RequestBody KeyWordSearchDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.searchUserResult(dto, request, result);
        return result;
    }

    @PostMapping("/getMyCommentList")
    @ApiOperation("获取用户中心评论列表处理器")
    public BaseResult getMyCommentList(@RequestBody KeyWordSearchPageDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getMyCommentListParamCheck(dto, result)) {
            return result;
        }
        articleCommentService.getMyCommentList(dto, request, result);
        return result;
    }

    @GetMapping("/removeMyArticle")
    @ApiOperation("用户中心删除文章处理器")
    public BaseResult removeMyArticle(@RequestParam("articleId") Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        userService.removeMyArticle(articleId, request, result);
        return result;
    }

    /**
     * 获取用户反馈列表
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getFeedbackList")
    @ApiOperation("获取用户反馈处理器")
    public BaseResult getFeedbackList(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!publicPageParamCheck(dto, result)) {
            return result;
        }
        userService.getFeedbackList(dto, request, result);
        return result;
    }

    /**
     * 获取用户反馈详情
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getFeedbackInfo/{id}")
    @ApiOperation("获取用户反馈详情处理器")
    public BaseResult getFeedbackInfo(@PathVariable("id") Integer id, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.getFeedbackInfo(id, request, result);
        return result;
    }

    /**
     * 获取用户积分交易流水
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getPointTradeFlow")
    @ApiOperation("获取用户积分交易流水")
    public BaseResult getPointTradeFlow(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!publicPageParamCheck(dto, result)) {
            return result;
        }
        userService.getPointTradeFlow(dto, request, result);
        return result;
    }

    /**
     * 用户签到
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/sign")
    @ApiOperation("用户签到")
    public BaseResult sign(HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.sign(request, result);
        return result;
    }

    /**
     * 获取用户举报列表
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getReportList")
    @ApiOperation("获取用户举报处理器")
    public BaseResult getReportList(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!publicPageParamCheck(dto, result)) {
            return result;
        }
        userService.getReportList(dto, request, result);
        return result;
    }

    /**
     * 获取用户举报详情
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getReportInfo/{id}")
    @ApiOperation("获取用户举报详情处理器")
    public BaseResult getReportInfo(@PathVariable("id") Integer id, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.getReportInfo(id, request, result);
        return result;
    }

    /**
     * 获取用户订单列表
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getUserOrderList")
    @ApiOperation("获取用户订单列表处理器")
    public BaseResult getUserOrderList(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!publicPageParamCheck(dto, result)) {
            return result;
        }
        userService.getUserOrderList(dto, request, result);
        return result;
    }

    /**
     * 获取系统消息列表
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/getSystemMessage")
    @ApiOperation("获取系统消息列表处理器")
    public BaseResult getSystemMessage(@RequestBody BaseRequest dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        if (!publicPageParamCheck(dto, result)) {
            return result;
        }
        systemMessageService.getSystemMessage(dto, request, result);
        return result;
    }

    /**
     * 设置系统消息已读
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/readSysMessage")
    @ApiOperation("设置系统消息已读处理器")
    public BaseResult readSysMessage(@RequestBody ReadMessageDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        systemMessageService.readSysMessage(dto, request, result);
        return result;
    }



    /**
     * BaseRequest公共分页参数验证
     * @param dto
     * @param result
     * @return
     */
    private boolean publicPageParamCheck(BaseRequest dto, BaseResult result) {
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

    /**
     * 获取用户发布列表参数验证
     * @param dto
     * @param result
     * @return
     */
    private boolean getArticlePublishListParamCheck(BaseRequest dto, BaseResult result) {
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

    /**
     * 获取用户收藏列表参数验证
     * @param dto
     * @param result
     * @return
     */
    private boolean getArticleCollListParamCheck(BaseRequest dto, BaseResult result) {
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


    /**
     * 修改个人信息参数验证
     * @param dto
     * @param result
     * @return
     */
    private boolean updateSelfInfoParamCheck(UpdateSelfInfoDTO dto, BaseResult result) {
        String userNamePattern = "^[A-Za-z0-9-_]+$";
        Pattern userNameR = Pattern.compile(userNamePattern);
        Matcher userNameM = userNameR.matcher(dto.getUserName());
        if (!userNameM.matches()) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("用户名必须由数字、英文字母组成");
            return false;
        }

        if (StringUtils.hasLength(dto.getPhone())) {
            if (!CheckUtil.checkPhone(dto.getPhone())) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("手机号格式错误");
                return false;
            }
        }

        if (StringUtils.hasLength(dto.getEmail())) {
            if (!CheckUtil.checkEmail(dto.getEmail())) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("邮箱格式错误");
                return false;
            }
        }
        return true;
    }

    /**
     * 获取用户中心评论列表参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean getMyCommentListParamCheck(KeyWordSearchPageDTO dto, BaseResult result) {
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
