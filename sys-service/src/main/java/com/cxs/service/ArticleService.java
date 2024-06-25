package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.article.AdminGetArticleListDTO;
import com.cxs.dto.admin.article.AdminUpdateArticleDTO;
import com.cxs.dto.admin.article.GetReviewedListDTO;
import com.cxs.dto.admin.article.ReviewedArticleDTO;
import com.cxs.dto.article.DownloadFileDTO;
import com.cxs.dto.article.GetArticleListDTO;
import com.cxs.dto.article.SaveOrUpdateArticleDTO;
import com.cxs.dto.article.SystemRecommendArticleDTO;
import com.cxs.model.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ArticleService extends IService<Article> {

    /**
     * 发布文章
     * @param dto
     * @param request
     * @param result
     */
    void saveArticle(SaveOrUpdateArticleDTO dto, MultipartFile file, HttpServletRequest request, BaseResult result);

    /**
     * 修改文章
     * @param dto
     * @param request
     * @param result
     */
    void updateArticle(SaveOrUpdateArticleDTO dto, MultipartFile file, HttpServletRequest request, BaseResult result);

    /**
     * 获取文章列表
     * @param dto
     * @param result
     */
    void getArticleList(GetArticleListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户获取文章详情
     * @param articleId
     * @param request
     * @param result
     */
    void getArticleInfo(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 阅读文章
     * @param articleId
     * @param request
     * @param result
     */
    void readArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 点赞文章
     * @param articleId
     * @param request
     * @param result
     */
    void likeArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 收藏文章
     * @param articleId
     * @param request
     * @param result
     */
    void collectionArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 记录文章搜索流水
     * @param articleId
     * @param request
     * @param result
     */
    void markArticleSearchLog(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 用户取消点赞
     * @param articleId
     * @param request
     * @param result
     */
    void cancelLikeArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 用户取消收藏
     * @param articleId
     * @param request
     * @param result
     */
    void cancelCollectionArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户打赏信息
     * @param articleId
     * @param request
     * @param result
     */
    void getUserRewardInfo(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 用户下载文件
     * @param dto
     * @param request
     * @param result
     */
    void downloadFileBase64(DownloadFileDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取官方推荐的文章列表
     * @param request
     * @param result
     */
    void getSystemRecommendArticleList(SystemRecommendArticleDTO dto,HttpServletRequest request, BaseResult result);

    /**
     * 获取热搜文章列表
     * @param request
     * @param result
     */
    void getHotArticleList(HttpServletRequest request, BaseResult result);

    /**
     * 文件下载处理
     * @param dto
     * @param request
     * @param response
     */
    void downloadFile(DownloadFileDTO dto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 管理员获取待审核列表
     * @param dto
     * @param request
     * @param result
     */
    void getReviewedList(GetReviewedListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户任意状态文章
     * @param articleId
     * @param request
     * @param result
     */
    void getMyAnyStatusArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 文章文件下载前置检查
     * @param dto
     * @param request
     * @param result
     */
    void downloadBeforeValid(DownloadFileDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 文件下载
     * @param k
     * @param request
     * @param response
     */
    void download(String k, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取首页文章列表
     * @param dto
     * @param request
     * @param result
     */
    void getArticleMainList(GetArticleListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取首页文章列表
     * @param dto
     * @param request
     * @param result
     */
    void getArticleIndexList(GetArticleListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取文章详情
     * @param articleId
     * @param request
     * @param result
     */
    void adminGetArticleInfo(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 审核文章
     * @param dto
     * @param request
     * @param result
     */
    void reviewedArticle(ReviewedArticleDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员下载文件
     * @param fileId
     * @param request
     * @param response
     */
    void adminDownload(String fileId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 管理员修改文章
     * @param dto
     * @param request
     * @param result
     */
    void adminUpdateArticle(AdminUpdateArticleDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员删除文章
     * @param articleId
     * @param request
     * @param result
     */
    void adminDeleteArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取文章列表
     * @param dto
     * @param request
     * @param result
     */
    void adminGetArticleList(AdminGetArticleListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改时获取文章详情
     * @param articleId
     * @param request
     * @param result
     */
    void getUpdateArticleInfo(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 获得文章文件
     * @param articleId
     * @param request
     * @param response
     */
    void getArticleFileInfo(Integer articleId, HttpServletRequest request, HttpServletResponse response);
}
