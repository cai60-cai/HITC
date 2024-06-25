package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文章阅读表
 * @TableName t_article_read
 */
@TableName(value ="t_article_read")
@Data
public class ArticleRead implements Serializable {
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
     * 阅读时间
     */
    private LocalDateTime readTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}