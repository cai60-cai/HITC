package com.cxs.vo.user;

import lombok.Data;


@Data
public class UserArticleCollListVO {
    private Integer id;

    private Integer articleId;
    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章评分
     */
    private Float articleRate;

    /**
     * 是否原创
     */
    private Integer articleIsSelf;

    /**
     * 文章作者
     */
    private String author;

    /**
     * 1、正常 -1、原文章已删除
     */
    private Integer status;
}
