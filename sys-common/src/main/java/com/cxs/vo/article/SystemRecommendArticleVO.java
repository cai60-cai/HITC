package com.cxs.vo.article;

import lombok.Data;

@Data
public class SystemRecommendArticleVO {

    private Integer articleId;

    private String articleTitle;

    private Integer readCount;

    private String typeName;
}
