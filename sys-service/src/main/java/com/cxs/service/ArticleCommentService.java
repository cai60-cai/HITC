package com.cxs.service;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.dto.KeyWordSearchPageDTO;
import com.cxs.dto.article.comment.GetArticleCommentListDTO;
import com.cxs.dto.article.comment.OperaCommentDTO;
import com.cxs.dto.article.comment.PublishCommentDTO;
import com.cxs.model.ArticleComment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface ArticleCommentService extends IService<ArticleComment> {

    /**
     * 用户中心获取我的评论列表
     * @param dto
     * @param request
     * @param result
     */
    void getMyCommentList(KeyWordSearchPageDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除评论
     * @param commentId
     * @param request
     * @param result
     */
    void deleteComment(Integer commentId, HttpServletRequest request, BaseResult result);

    /**
     * 创建评论
     * @param dto
     * @param request
     * @param result
     */
    void publishComment(PublishCommentDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取评论列表数据
     * @param dto
     * @param request
     * @param result
     */
    void getArticleCommentList(GetArticleCommentListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 评论操作、设为精华、置顶
     * @param dto
     * @param request
     * @param result
     */
    void operaComment(OperaCommentDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取评论详情
     * @param commentId
     * @param request
     * @param result
     */
    void adminGetCommentInfo(Integer commentId, HttpServletRequest request, BaseResult result);
}
