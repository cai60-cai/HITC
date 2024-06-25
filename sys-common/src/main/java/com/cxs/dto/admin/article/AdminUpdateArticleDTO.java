package com.cxs.dto.admin.article;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

  
@Data
public class AdminUpdateArticleDTO {

    @NotNull(message = "articleId为必传项")
    private Integer articleId;

    /**
     * 文章标题
     */
    @NotBlank(message = "articleTitle为必传项")
    @ParamLengthValid(max = 20, message = "articleTitle长度0-20之间")
    private String articleTitle;

    /**
     * 文章摘要
     */
    @NotBlank(message = "articleAbstract为必传项")
    @ParamLengthValid(max = 130, message = "articleTitle长度0-130之间")
    private String articleAbstract;

    /**
     * 文章分类
     */
    private Integer articleType;

    /**
     * 是否原创
     */
    @NotNull(message = "articleIsSelf为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "articleIsSelf可选值{0,1}")
    private Integer articleIsSelf;

    /**
     * 原文链接
     */
    private String originalLink;
}
