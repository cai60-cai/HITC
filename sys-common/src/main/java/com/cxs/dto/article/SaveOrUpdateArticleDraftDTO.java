package com.cxs.dto.article;

import com.cxs.dto.tag.TagDTO;
import lombok.Data;

import java.util.List;

  
@Data
public class SaveOrUpdateArticleDraftDTO {

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

    private Integer importance;

    /**
     * 文章详情
     */
    private String articleDetail;

    /**
     * 文章标签
     */
    private List<TagDTO> tags;
}
