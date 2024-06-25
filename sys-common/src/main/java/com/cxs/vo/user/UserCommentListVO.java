package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserCommentListVO {

    private Integer commentId;

    /**
     * 评论文章
     */
    private SimpleArticleVO article;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    /**
     * 精华
     */
    private Integer commentEssence;

    private SimpleUserVO commentTo;

    @Data
    public static class SimpleArticleVO{
        private Integer articleId;
        private String articleTitle;
    }

}
