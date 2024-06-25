package com.cxs.vo.article;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserSelfArticleDetailVO {
    private Integer articleId;

    /**
     * 文章标题
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

    /**
     * 文章详情
     */
    private String articleDetail;

    /**
     * 依赖包文件id
     */
    private String articleFileId;

    /**
     * 文章状态,0、未审核 1、已通过 2、未通过
     */
    private Integer articleStatus;

    /**
     * 文章评分
     */
    private Float articleRate;

    /**
     * 发布用户
     */
    private String articleBelongUserId;

    /**
     * 是否官方推荐
     */
    private Integer articleIsRecommend;


    /**
     * 是否原创
     */
    private Integer articleIsSelf;

    /**
     * 原文链接
     */
    private String originalLink;

    /**
     * 文章创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 文章修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
