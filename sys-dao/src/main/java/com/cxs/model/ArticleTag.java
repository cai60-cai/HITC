package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章-标签关联表
 * @TableName t_article_tag
 */
@TableName(value ="t_article_tag")
@Data
public class ArticleTag implements Serializable {
    /**
     * 文章id
     */
    @TableId
    private Integer articleId;

    /**
     * 标签id
     */
    @TableId
    private Integer tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}