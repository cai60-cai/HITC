package com.cxs.dto.article;

import com.cxs.dto.tag.TagDTO;
import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;
import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

  
@Data
public class SaveOrUpdateArticleDTO {

    private Integer articleId;


    @ParamRangeValid(ranges = {"0", "1"}, message = "fileFlag可选值{0,1}")
    private Integer fileFlag;

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
     * 文章封面
     */
    private String articleCover;

    /**
     * 文章分类
     */
    private Integer articleType;

    @NotNull(message = "articleIsSelf为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "articleIsSelf可选值{0,1}")
    private Integer articleIsSelf;

    /**
     * 文章详情
     */
    private String articleDetail;

    /**
     * 原文链接
     */
    private String originalLink;

    /**
     * 文章标签
     */
    private String tags;
}
