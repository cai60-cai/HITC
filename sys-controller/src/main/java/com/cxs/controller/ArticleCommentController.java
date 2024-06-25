package com.cxs.controller;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.dto.KeyWordSearchPageDTO;
import com.cxs.dto.article.comment.GetArticleCommentListDTO;
import com.cxs.dto.article.comment.LikeCommentDTO;
import com.cxs.dto.article.comment.OperaCommentDTO;
import com.cxs.dto.article.comment.PublishCommentDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ArticleCommentLikeService;
import com.cxs.service.ArticleCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@Api(tags = "文章评论控制器")
@RequestMapping("/article-comment")
public class ArticleCommentController {

    @Autowired
    private ArticleCommentService articleCommentService;

    @Autowired
    private ArticleCommentLikeService articleCommentLikeService;

    @GetMapping("/deleteComment")
    @ApiOperation("删除评论处理器")
    public BaseResult deleteComment(@RequestParam("commentId") Integer commentId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleCommentService.deleteComment(commentId, request, result);
        return result;
    }

    @PostMapping("/publishComment")
    @ApiOperation("发布评论处理器")
    public BaseResult publishComment(@RequestBody @Validated PublishCommentDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!publishCommentParamCheck(dto, result)) {
            return result;
        }
        articleCommentService.publishComment(dto, request, result);
        return result;
    }

    @PostMapping("/likeComment")
    @ApiOperation("评论点赞处理器")
    public BaseResult likeComment(@RequestBody @Validated LikeCommentDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleCommentLikeService.likeComment(dto, request, result);
        return result;
    }

    @PostMapping("/unLikeComment")
    @ApiOperation("评论取消点赞处理器")
    public BaseResult unLikeComment(@RequestBody @Validated LikeCommentDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleCommentLikeService.unLikeComment(dto, request, result);
        return result;
    }

    @PostMapping("/operaComment")
    @ApiOperation("评论功能操作处理器")
    public BaseResult operaComment(@RequestBody @Validated OperaCommentDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleCommentService.operaComment(dto, request, result);
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
     * 发布评论参数处理
     * @param dto
     * @param result
     * @return
     */
    private boolean publishCommentParamCheck(PublishCommentDTO dto, BaseResult result) {
        if (!ObjectUtils.isEmpty(dto.getParentCommentId())) {
            if (!StringUtils.hasLength(dto.getCommentTo())) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("发布子评论时评论对象不能为空");
                return false;
            }
        }
        return true;
    }
}
