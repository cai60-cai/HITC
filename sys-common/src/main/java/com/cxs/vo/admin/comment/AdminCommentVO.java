package com.cxs.vo.admin.comment;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class AdminCommentVO {

    private Integer commentId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    private List<AdminCommentVO> childCommentList;
}
