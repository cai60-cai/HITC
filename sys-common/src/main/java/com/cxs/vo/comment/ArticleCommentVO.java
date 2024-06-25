package com.cxs.vo.comment;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.vo.user.SimpleUserVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleCommentVO {
    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论人
     */
    private SimpleUserVO commentFrom;

    /**
     * 评论对象
     */
    private SimpleUserVO commentTo;

    /**
     * 父级评论id
     */
    private Integer parentCommentId;

    /**
     * 评论时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;


    private String commentTimeFormat;

    /**
     * 精华
     */
    private Boolean commentEssence;

    /**
     * 置顶
     */
    private Boolean top;

    private Boolean likeStatus;

    private Integer likeCount;

    private List<ArticleCommentChildVO> children = new ArrayList<>();


    @Data
    public static class ArticleCommentChildVO{
        /**
         * 评论id
         */
        private Integer commentId;

        /**
         * 文章id
         */
        private Integer articleId;

        /**
         * 评论内容
         */
        private String commentContent;

        /**
         * 评论人
         */
        private SimpleUserVO commentFrom;

        /**
         * 评论对象
         */
        private SimpleUserVO commentTo;

        /**
         * 父级评论id
         */
        private Integer parentCommentId;

        /**
         * 评论时间
         */
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime commentTime;


        private String commentTimeFormat;

        /**
         * 精华
         */
        private Boolean commentEssence;

        /**
         * 置顶
         */
        private Boolean top;

        private Boolean likeStatus;

        private Integer likeCount;
    }
}
