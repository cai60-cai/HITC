package com.cxs.controller;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.service.impl.WebSocketSysMessage;
import com.cxs.dto.SendEmailCodeDTO;
import com.cxs.dto.UserRegirsterDTO;
import com.cxs.dto.article.GetArticleListDTO;
import com.cxs.dto.article.SystemRecommendArticleDTO;
import com.cxs.dto.article.comment.GetArticleCommentListDTO;
import com.cxs.dto.profile.CheckEmailBindDTO;
import com.cxs.dto.profile.CheckUsernameDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ArticleCommentService;
import com.cxs.service.ArticleService;
import com.cxs.service.BaseService;
import com.cxs.service.ExternalLinkService;
import com.cxs.service.NavLinkService;
import com.cxs.service.NoticeInfoService;
import com.cxs.service.PointRechargeTypeService;
import com.cxs.service.RealtionQuestionService;
import com.cxs.service.SystemService;
import com.cxs.service.TagService;
import com.cxs.service.TechnologyTypeService;
import com.cxs.service.UserService;
import com.cxs.utils.CheckUtil;
import com.cxs.vo.message.SysMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/main")
@Api(tags = "首页控制器")
public class MainController {

    @Autowired
    private NavLinkService navLinkService;

    @Autowired
    private TechnologyTypeService technologyTypeService;

    @Autowired
    private ExternalLinkService externalLinkService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private NoticeInfoService noticeInfoService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private UserService userService;

    @Autowired
    private RealtionQuestionService realtionQuestionService;

    @Autowired
    private ArticleCommentService articleCommentService;

    @Autowired
    private PointRechargeTypeService pointRechargeTypeService;

    @Autowired
    private WebSocketSysMessage webSocketSysMessage;


    @GetMapping("/nav/getLinkList")
    @ApiOperation("获取导航链接列表处理器")
    public BaseResult getLinkList(){
        BaseResult result = BaseResult.ok();
        navLinkService.getLinkList(result);
        return result;
    }

    @GetMapping("/type/getTechnologyType")
    @ApiOperation("获取技术分类列表处理器")
    public BaseResult getTechnologyTypeList(){
        BaseResult result = BaseResult.ok();
        technologyTypeService.getTechnologyTypeList(result);
        return result;
    }

    @GetMapping("/link/getExternalLinkList")
    @ApiOperation("获取外部链接列表处理器")
    public BaseResult getExternalLinkList(){
        BaseResult result = BaseResult.ok();
        externalLinkService.getExternalLinkList(result);
        return result;
    }

    @GetMapping("/tag/getTagList")
    @ApiOperation("获取标签列表处理器")
    public BaseResult getTagList(){
        BaseResult result = BaseResult.ok();
        tagService.getTagList(result);
        return result;
    }

    @PostMapping("/article/getArticleIndexList")
    @ApiOperation("获取文章列表处理器")
    public BaseResult getArticleIndexList(@RequestBody GetArticleListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getArticleListParamHandle(dto, result)) {
            return result;
        }
        articleService.getArticleIndexList(dto, request, result);
        return result;
    }

    @PostMapping("/article/getArticleList")
    @ApiOperation("获取文章列表处理器")
    public BaseResult getArticleList(@RequestBody GetArticleListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getArticleListParamHandle(dto, result)) {
            return result;
        }
        articleService.getArticleList(dto, request, result);
        return result;
    }

    @PostMapping("/article/getArticleMainList")
    @ApiOperation("获取首页文章列表处理器")
    public BaseResult getArticleMainList(@RequestBody GetArticleListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getArticleListParamHandle(dto, result)) {
            return result;
        }
        articleService.getArticleMainList(dto, request, result);
        return result;
    }

    @PostMapping("/article/getSystemRecommendArticleList")
    @ApiOperation("获取官方推荐文章列表处理器")
    public BaseResult getSystemRecommendArticleList(@RequestBody SystemRecommendArticleDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(dto.getPageNum()) || dto.getPageNum() < -1 || dto.getPageNum() == 0) {
            dto.setPageNum(1);
        }
        if (ObjectUtils.isEmpty(dto.getPageSize()) || dto.getPageSize() < 0) {
            dto.setPageNum(10);
        }
        articleService.getSystemRecommendArticleList(dto, request, result);
        return result;
    }

    @GetMapping("/article/getHotArticleList")
    @ApiOperation("获取热搜文章列表处理器")
    public BaseResult getHotArticleList(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.getHotArticleList(request, result);
        return result;
    }

    @GetMapping("/article/getArticleInfo")
    @ApiOperation("获取文章详情处理器")
    public BaseResult getArticleInfo(Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
            return result;
        }
        articleService.getArticleInfo(articleId, request, result);
        return result;
    }

    @GetMapping("/system/getAboutSysInfo")
    @ApiOperation("获取关于本站信息处理器")
    public BaseResult getAboutSysInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        systemService.getAboutSysInfo(request, result);
        return result;
    }

    @GetMapping("/article/readArticle")
    @ApiOperation("用户阅读文章处理器")
    public BaseResult readArticle(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.readArticle(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/article/likeArticle")
    @ApiOperation("用户点赞文章处理器")
    public BaseResult likeArticle(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.likeArticle(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/article/cancelLikeArticle")
    @ApiOperation("用户取消点赞文章处理器")
    public BaseResult cancelLikeArticle(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.cancelLikeArticle(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/article/collectionArticle")
    @ApiOperation("用户收藏文章处理器")
    public BaseResult collectionArticle(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.collectionArticle(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/article/cancelCollectionArticle")
    @ApiOperation("用户取消收藏文章处理器")
    public BaseResult cancelCollectionArticle(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.cancelCollectionArticle(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/article/markArticleSearchLog")
    @ApiOperation("用户搜索文章流水记录处理器")
    public BaseResult markArticleSearchLog(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.markArticleSearchLog(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/reward/getUserRewardInfo")
    @ApiOperation("获取用户打赏信息处理器")
    public BaseResult getUserRewardInfo(@RequestParam(value = "articleId", required = false) Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (ObjectUtils.isEmpty(articleId)) {
            result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",文章id为必传项");
        } else {
            articleService.getUserRewardInfo(articleId, request, result);
        }
        return result;
    }

    @GetMapping("/notice/getPushNoticeList")
    @ApiOperation("获取公告列表处理器")
    public BaseResult getPushNoticeList(@RequestParam(value = "type", required = false) Integer type,  HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        noticeInfoService.getPushNoticeList(type, request, result);
        return result;
    }

    @GetMapping("/notice/getNoticeInfo")
    @ApiOperation("获取公告详情处理器")
    public BaseResult getNoticeInfo(@RequestParam("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        noticeInfoService.getNoticeInfo(id, request, result);
        return result;
    }

    @GetMapping("/system/getSystemInfo")
    @ApiOperation("获取关于系统本站详情处理器")
    public BaseResult getSystemInfo(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        systemService.getSystemInfo(request, result);
        return result;
    }

    @PostMapping("/user/sendEmailCode")
    @ApiOperation("发送验证码处理器")
    public BaseResult sendEmailCode(@RequestBody @Validated SendEmailCodeDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!sendEmailCodeParamCheck(dto, result)) {
            return result;
        }
        baseService.sendEmailCode(dto, request, result);
        return result;
    }

    @PostMapping("/user/register")
    @ApiOperation("新用户注册处理器")
    public BaseResult register(@RequestBody @Validated UserRegirsterDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!registerParamCheck(dto, result)) {
            return result;
        }
        userService.register(dto, request, result);
        return result;
    }

    @GetMapping("/question/questionList")
    @ApiOperation("相关问题列表处理器")
    public BaseResult questionList(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        realtionQuestionService.questionList(result, request);
        return result;
    }

    @GetMapping("/question/questionInfo/{id}")
    @ApiOperation("相关问题详情处理器")
    public BaseResult questionInfo(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        realtionQuestionService.questionInfo(id, result, request);
        return result;
    }

    /**
     * 查询用户名是否存在
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/user/checkUserNameExist")
    @ApiOperation("查询用户名是否存在处理器")
    public BaseResult checkUserNameExist(@RequestBody @Validated CheckUsernameDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.checkUserNameExist(dto, request, result);
        return result;
    }

    /**
     * 查询邮箱是否绑定用户超过3个
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/user/checkEmailBindStatus")
    @ApiOperation("邮箱是否绑定用户超过3个处理器")
    public BaseResult checkEmailBindStatus(@RequestBody @Validated CheckEmailBindDTO dto, HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        userService.checkEmailBindStatus(dto, request, result);
        return result;
    }

    @PostMapping("/comment/getArticleCommentList")
    @ApiOperation("获取评论列表处理器")
    public BaseResult getArticleCommentList(@RequestBody GetArticleCommentListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!publicPageParamCheck(dto, result)) {
            return result;
        }
        articleCommentService.getArticleCommentList(dto, request, result);
        return result;
    }

    @GetMapping("/rechangeType/getPointRechargeTypeList")
    @ApiOperation("获取积分充值类型处理器")
    public BaseResult getPointRechargeTypeList(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        pointRechargeTypeService.getPointRechargeTypeList(request, result);
        return result;
    }

    @GetMapping("/send")
    @ApiOperation("获取积分充值类型处理器")
    public BaseResult send(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        SysMessageVO vo = new SysMessageVO();
        vo.setId(1000);
        vo.setCreateTime(LocalDateTime.now());
        vo.setIsRead(Boolean.FALSE);
        vo.setMessageContent("吃的是草");
        webSocketSysMessage.sendMessageText("67489e02c9b44da6aa33735ee9beb4d7", vo);
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
     * 用户注册参数检查
     * @param dto
     * @param result
     * @return
     */
    private boolean registerParamCheck(UserRegirsterDTO dto, BaseResult result) {
        if (!CheckUtil.checkEmail(dto.getEmail())) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("邮箱格式错误");
            return false;
        }
        return true;
    }

    /**
     * 发送验证码参数检查
     * @param dto
     * @param result
     * @return
     */
    private boolean sendEmailCodeParamCheck(SendEmailCodeDTO dto, BaseResult result) {
        if (!CheckUtil.checkEmail(dto.getEmail())) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("邮箱格式错误");
            return false;
        }
        return true;
    }

    /**
     * 获取文章列表参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean getArticleListParamHandle(GetArticleListDTO dto, BaseResult result) {
        if (dto.getPageNum() < 1) {
            dto.setPageNum(1);
        }
        if (dto.getPageSize() < 0) {
            dto.setPageSize(10);
        }
        return true;
    }
}
