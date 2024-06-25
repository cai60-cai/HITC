package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.dto.article.DownloadFileDTO;
import com.cxs.dto.article.SaveOrUpdateArticleDTO;
import com.cxs.dto.article.SaveOrUpdateArticleDraftDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ArticleDraftService;
import com.cxs.service.ArticleService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "文章资源控制器")
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleDraftService articleDraftService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/getArticleDraft")
    @ApiOperation("获取用户文章草稿处理器")
    public BaseResult getArticleDraft(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleDraftService.getArticleDraft(request, result);
        return result;
    }

    @PostMapping("/saveOrUpdateDraft")
    @ApiOperation("保存修改用户文章草稿处理器")
    public BaseResult saveOrUpdateDraft(@RequestBody SaveOrUpdateArticleDraftDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleDraftService.saveOrUpdateDraft(dto, request, result);
        return result;
    }

    @GetMapping("/deleteDraft")
    @ApiOperation("删除用户文章草稿处理器")
    public BaseResult deleteDraft(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleDraftService.deleteDraft(request, result);
        return result;
    }

    @PostMapping("/saveOrUpdateArticle")
    @ApiOperation("保存修改用户文章处理器")
    public BaseResult saveOrUpdateArticle(SaveOrUpdateArticleDTO dto, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        Integer articleId = dto.getArticleId();
        if (ObjectUtils.isEmpty(articleId)) {
            articleService.saveArticle(dto, file, request, result);
        } else {
            articleService.updateArticle(dto, file, request, result);
        }
        return result;
    }

    @PostMapping("/downloadFileBase64")
    @ApiOperation("Base64文件下载处理器")
    public BaseResult downloadFileBase64(@RequestBody @Validated DownloadFileDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.downloadFileBase64(dto, request, result);
        return result;
    }

    @PostMapping("/downloadFile")
    @ApiOperation("文件下载处理器")
    public void downloadFile(@RequestBody @Validated DownloadFileDTO dto, HttpServletRequest request, HttpServletResponse response){
        articleService.downloadFile(dto, request, response);
    }

    @PostMapping("/downloadBeforeValid")
    @ApiOperation("文件下载前置检查处理器")
    public BaseResult downloadBeforeValid(@RequestBody @Validated DownloadFileDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.downloadBeforeValid(dto, request, result);
        return result;
    }

    @GetMapping("/download")
    @ApiOperation("文件下载处理器")
    public void download(@RequestParam("k") String k, HttpServletRequest request, HttpServletResponse response){
        articleService.download(k, request, response);
    }

    @GetMapping("/getMyAnyStatusArticle")
    @ApiOperation("获取用户任意状态文章处理器")
    public BaseResult getMyAnyStatusArticle(@RequestParam("articleId") Integer articleId,HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.getMyAnyStatusArticle(articleId, request, result);
        return result;
    }

    @GetMapping("/getUpdateArticleInfo/{articleId}")
    @ApiOperation("获取文章详情处理器")
    public BaseResult getUpdateArticleInfo(@PathVariable("articleId") Integer articleId, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        articleService.getUpdateArticleInfo(articleId, request, result);
        return result;
    }

    @GetMapping("/getArticleFileInfo/{articleId}")
    @ApiOperation("获取文章文件处理器")
    public void getArticleFileInfo(@PathVariable("articleId") Integer articleId, HttpServletRequest request, HttpServletResponse response){
        articleService.getArticleFileInfo(articleId, request, response);
    }
}
