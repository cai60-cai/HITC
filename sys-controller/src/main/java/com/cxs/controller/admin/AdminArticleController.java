package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.article.AdminGetArticleListDTO;
import com.cxs.dto.admin.article.AdminUpdateArticleDTO;
import com.cxs.dto.admin.article.GetReviewedListDTO;
import com.cxs.dto.admin.article.ReviewedArticleDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ArticleService;
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
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/article")
@Api(tags = "管理员文章控制器")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/getReviewedList")
    @ApiOperation("管理员获取待审核文章列表处理器")
    public BaseResult getReviewedList(@RequestBody GetReviewedListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!getReviewedListParamCheck(dto, result)) {
            return result;
        }
        articleService.getReviewedList(dto, request, result);
        return result;
    }

    @PostMapping("/getArticleList")
    @ApiOperation("管理员获取文章列表处理器")
    public BaseResult adminGetArticleList(@RequestBody AdminGetArticleListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!adminGetArticleListParamCheck(dto, result)) {
            return result;
        }
        articleService.adminGetArticleList(dto, request, result);
        return result;
    }

    private boolean adminGetArticleListParamCheck(AdminGetArticleListDTO dto, BaseResult result) {
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

    @PostMapping("/reviewedArticle")
    @ApiOperation("管理员审核文章处理器")
    @MarkLog(desc = "管理员审核文章")
    public BaseResult reviewedArticle(@RequestBody @Validated ReviewedArticleDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!reviewedArticleParamCheck(dto, result)) {
            return result;
        }
        articleService.reviewedArticle(dto, request, result);
        return result;
    }

    @PostMapping("/updateArticle")
    @ApiOperation("管理员修改文章处理器")
    @MarkLog(desc = "管理员修改文章")
    public BaseResult updateArticle(@RequestBody @Validated AdminUpdateArticleDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.adminUpdateArticle(dto, request, result);
        return result;
    }

    @GetMapping("/download")
    @ApiOperation("管理员文件下载处理器")
    public void adminDownload(@RequestParam("fileId") String fileId, HttpServletRequest request, HttpServletResponse response){
        articleService.adminDownload(fileId, request, response);
    }

    @DeleteMapping("/deleteArticle/{articleId}")
    @ApiOperation("管理员删除文章处理器")
    @MarkLog(desc = "管理员删除文章")
    public BaseResult adminDeleteArticle(@PathVariable("articleId") Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.adminDeleteArticle(articleId, request, result);
        return result;
    }

    private boolean reviewedArticleParamCheck(ReviewedArticleDTO dto, BaseResult result) {
        Float articleRate = dto.getArticleRate();
        if (null != articleRate) {
            if (articleRate < 0f || articleRate > 5.0f) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("articleRate参数范围0~5");
                return false;
            }
        }
        Integer articleDownloadPoint = dto.getArticleDownloadPoint();
        if (null != articleDownloadPoint) {
            if (articleDownloadPoint < 0) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode()).setMsg("articleDownloadPoint参数错误");
                return false;
            }
        }
        return true;
    }

    @GetMapping("/getArticleInfo/{articleId}")
    @ApiOperation("管理员获取文章详情处理器")
    public BaseResult getArticleInfo(@PathVariable("articleId") Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.adminGetArticleInfo(articleId, request, result);
        return result;
    }

    /**
     * 管理员获取待审核文章列表参数验证
     * @param dto
     * @param result
     * @return
     */
    private boolean getReviewedListParamCheck(GetReviewedListDTO dto, BaseResult result) {
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
