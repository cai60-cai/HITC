package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户反馈回复表
 * @TableName t_feedback_reply
 */
@TableName(value ="t_feedback_reply")
@Data
public class FeedbackReply implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer replyId;

    /**
     * 反馈id
     */
    private Integer feedbackId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 处理人
     */
    private String adminId;

    /**
     * 处理时间
     */
    private LocalDateTime replyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}