package com.cxs.vo.article;

import lombok.Data;

@Data
public class ArticleHotSearchVO {
    private Integer articleId;
    private String articleTitle;
    private Integer searchCount;
}
