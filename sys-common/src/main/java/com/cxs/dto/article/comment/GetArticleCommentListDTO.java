package com.cxs.dto.article.comment;

import com.cxs.base.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

  
@Data
public class GetArticleCommentListDTO extends BaseRequest {

    @NotNull(message = "articleId为必传项")
    private Integer articleId;
}
