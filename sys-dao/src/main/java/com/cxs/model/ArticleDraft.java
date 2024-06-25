package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文章草稿表
 * @TableName t_article_draft
 */
@TableName(value ="t_article_draft")
@Data
public class ArticleDraft implements Serializable {
    /**
     * 草稿id，userId
     */
    @TableId
    private String userId;

    /**
     * 文章id
     */
    private String articleTitle;

    /**
     * 文章摘要
     */
    private String articleAbstract;

    /**
     * 文章封面
     */
    private String articleCover;

    /**
     * 文章分类
     */
    private Integer articleType;

    private Integer importance;

    /**
     * 文章详情
     */
    private String articleDetail;

    /**
     * 文章标签
     */
    private String articleTags;

    /**
     * 草稿创建时间
     */
    private LocalDateTime createTime;

    /**
     * 草稿修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}