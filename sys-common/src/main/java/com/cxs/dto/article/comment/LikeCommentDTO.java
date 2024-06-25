package com.cxs.dto.article.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class LikeCommentDTO {
    @NotNull(message = "commentId为必传项")
    private Integer commentId;
}
