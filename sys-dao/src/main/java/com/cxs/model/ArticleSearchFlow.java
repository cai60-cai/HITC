package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文章搜索次数记录表
 * @TableName t_article_search_flow
 */
@TableName(value ="t_article_search_flow")
@Data
public class ArticleSearchFlow implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 搜索时间
     */
    private LocalDateTime searchTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}