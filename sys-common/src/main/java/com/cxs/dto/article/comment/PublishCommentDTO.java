package com.cxs.dto.article.comment;

import com.cxs.valid.annotation.ParamLengthValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

  
@Data
public class PublishCommentDTO {

    /**
     * 文章id
     */
    @NotNull(message = "articleId岂能为空")
    private Integer articleId;

    /**
     * 评论内容
     */
    @NotBlank(message = "commentContent岂能为空")
    @ParamLengthValid(max = 1000, message = "commentContent最大1000字符")
    private String commentContent;

    /**
     * 评论人
     */
    private String commentFrom;

    /**
     * 评论对象
     */
    private String commentTo;

    /**
     * 父级评论id
     */
    private Integer parentCommentId;
}
