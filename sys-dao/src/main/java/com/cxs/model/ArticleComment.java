package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 文章评论表
 * @TableName t_article_comment
 */
@TableName(value ="t_article_comment")
@Data
public class ArticleComment implements Serializable {
    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    /**
     * 评论id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论人
     */
    private String commentFrom;

    /**
     * 评论对象
     */
    private String commentTo;

    /**
     * 父级评论id
     */
    private Integer parentCommentId;

    /**
     * 评论时间
     */
    private LocalDateTime commentTime;

    /**
     * 精华
     */
    private Boolean commentEssence;

    /**
     * 置顶
     */
    private Boolean top;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}