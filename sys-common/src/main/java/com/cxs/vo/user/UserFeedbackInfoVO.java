package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class UserFeedbackInfoVO {
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
    private List<String> feedbackImages;

    /**
     * 状态，1、已处理 0、未处理
     */
    private Integer feedbackStatus;

    /**
     * 用户接收邮箱
     */
    private String feedbackEmail;

    /**
     * 反馈时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime feedbackTime;

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;
}
