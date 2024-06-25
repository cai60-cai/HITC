package com.cxs.vo.article;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDraftVO {
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

    /**
     * 文章详情
     */
    private String articleDetail;

    private Integer importance;

    /**
     * 文章标签
     */
    private List<Integer> tags;

    /**
     * 草稿创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 草稿修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
