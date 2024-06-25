package com.cxs.dto.article.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OperaCommentDTO {

    @NotNull(message = "commentId为必传项")
    private Integer commentId;

    /**
     * 精华
     */
    private Boolean commentEssence = Boolean.FALSE;

    /**
     * 置顶
     */
    private Boolean top = Boolean.FALSE;
}
