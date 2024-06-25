package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.article.comment.LikeCommentDTO;
import com.cxs.dto.article.comment.OperaCommentDTO;
import com.cxs.model.ArticleCommentLike;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author DELL
* @description 针对表【t_article_comment_like(文章评论点赞表)】的数据库操作Service
* @createDate 2024-05-27 18:37:20
*/
public interface ArticleCommentLikeService extends IService<ArticleCommentLike> {

    /**
     * 评论点赞
     * @param dto
     * @param request
     * @param result
     */
    void likeComment(LikeCommentDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 评论取消点赞
     * @param dto
     * @param request
     * @param result
     */
    void unLikeComment(LikeCommentDTO dto, HttpServletRequest request, BaseResult result);
}
