package com.cxs.vo.admin.article;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cxs.vo.article.FileVO;
import com.cxs.vo.tag.TagVO;
import com.cxs.vo.technology.TechnologyTypeVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class AdminArticleInfoVO {
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
     * 文章详情
     */
    private String articleDetail;

    /**
     * 发布用户
     */
    private String articleBelongUserId;

    /**
     * 是否官方推荐
     */
    private Integer articleIsRecommend;

    /**
     * 下载文章所需积分
     */
    private Integer articleDownloadPoint;

    /**
     * 是否原创
     */
    private Integer articleIsSelf;

    /**
     * 原文链接
     */
    private String originalLink;

    private Integer collectionNum;

    private Integer commentNum;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private TechnologyTypeVO type;

    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    private FileVO fileInfo;

    private List<TagVO> tags;
}
