package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 文章评论点赞表
 * @TableName t_article_comment_like
 */
@TableName(value ="t_article_comment_like")
@Data
public class ArticleCommentLike implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 所属用户
     */
    private String belongUserId;

    /**
     * 点赞时间
     */
    private LocalDateTime likeTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}