package com.cxs.vo.admin.article;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ArticleReviewedListVO {

    private Integer articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    private Integer articleStatus;

    /**
     * 分类名
     */
    private String typeName;

    /**
     * 原创
     */
    private Integer articleIsSelf;

    private Boolean downLoadFlag;

    /**
     * 文章创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
