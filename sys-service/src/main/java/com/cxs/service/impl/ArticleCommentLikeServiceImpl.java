package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.article.comment.LikeCommentDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ArticleCommentMapper;
import com.cxs.model.ArticleComment;
import com.cxs.model.ArticleCommentLike;
import com.cxs.service.ArticleCommentLikeService;
import com.cxs.mapper.ArticleCommentLikeMapper;
import com.cxs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author DELL
* @description 针对表【t_article_comment_like(文章评论点赞表)】的数据库操作Service实现
* @createDate 2024-05-27 18:37:20
*/
@Slf4j
@Service
public class ArticleCommentLikeServiceImpl extends ServiceImpl<ArticleCommentLikeMapper, ArticleCommentLike> implements ArticleCommentLikeService{

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleCommentLikeMapper articleCommentLikeMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Override
    public void likeComment(LikeCommentDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Integer commentId = dto.getCommentId();
            ArticleComment comment = articleCommentMapper.selectById(commentId);
            if (ObjectUtils.isEmpty(comment)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该评论不存在");
            } else {
                UserSubject userByToken = userService.getUserByToken(request);
                LambdaQueryWrapper<ArticleCommentLike> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ArticleCommentLike::getCommentId, commentId)
                        .eq(ArticleCommentLike::getUserId, userByToken.getId());
                Integer count = articleCommentLikeMapper.selectCount(queryWrapper);
                if (count >= 1) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",已点赞的评论不能重复点赞");
                } else {
                    ArticleCommentLike articleCommentLike = new ArticleCommentLike();
                    articleCommentLike.setCommentId(commentId);
                    articleCommentLike.setBelongUserId(comment.getCommentFrom());
                    articleCommentLike.setUserId(userByToken.getId());
                    articleCommentLike.setLikeTime(LocalDateTime.now());
                    int insert = articleCommentLikeMapper.insert(articleCommentLike);
                    if (insert != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户评论点赞失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户评论点赞接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void unLikeComment(LikeCommentDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Integer commentId = dto.getCommentId();
            ArticleComment comment = articleCommentMapper.selectById(commentId);
            if (ObjectUtils.isEmpty(comment)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该评论不存在");
            } else {
                UserSubject userByToken = userService.getUserByToken(request);
                LambdaQueryWrapper<ArticleCommentLike> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ArticleCommentLike::getCommentId, commentId)
                        .eq(ArticleCommentLike::getUserId, userByToken.getId());
                List<ArticleCommentLike> commentLikes = articleCommentLikeMapper.selectList(queryWrapper);
                if (commentLikes.size() < 1) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该评论还未点赞");
                } else {
                    ArticleCommentLike articleCommentLike = commentLikes.get(0);
                    articleCommentLikeMapper.deleteById(articleCommentLike.getId());
                }
            }

        } catch (Exception e) {
            log.error("用户评论取消点赞失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户评论取消点赞接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, dto, result);
        }
    }
}
