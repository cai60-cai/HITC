package com.cxs.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 文章表
 * @TableName t_article
 */
@TableName(value ="t_article")
@Data
public class Article implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
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
     * 下载文章所需积分
     */
    private Integer articleDownloadPoint;

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
     * 是否首页
     */
    private Integer articleIsIndex;

    /**
     * 原文链接
     */
    private String originalLink;

    /**
     * 备注描述
     */
    private String articleDesc;

    /**
     * 文章创建时间
     */
    private LocalDateTime createTime;

    /**
     * 文章修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
