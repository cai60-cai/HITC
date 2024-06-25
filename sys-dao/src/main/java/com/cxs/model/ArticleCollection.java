package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 文章收藏表
 * @TableName t_article_collection
 */
@TableName(value ="t_article_collection")
@Data
public class ArticleCollection implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章所属用户id
     */
    private String belongUserId;

    /**
     * 收藏时间
     */
    private LocalDateTime collectionTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}