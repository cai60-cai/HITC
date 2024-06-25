package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 用户反馈表
 * @TableName t_feedback
 */
@TableName(value ="t_feedback")
@Data
public class Feedback implements Serializable {
    /**
     * 反馈id
     */
    @TableId(type = IdType.AUTO)
    private Integer feedbackId;

    /**
     * 反馈类型
     */
    private String feedbackType;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 反馈图片
     */
    private String feedbackImages;

    /**
     * 状态，1、已处理 0、未处理
     */
    private Integer feedbackStatus;

    /**
     * 反馈用户
     */
    private String feedbackUser;

    /**
     * 用户接收邮箱
     */
    private String feedbackEmail;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}