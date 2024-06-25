package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 用户权限禁止定时表
 * @TableName t_user_auth_scheduled
 */
@TableName(value ="t_user_auth_scheduled")
@Data
public class UserAuthScheduled implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文件上传权限
     */
    private Boolean uploadAuth;

    /**
     * 发言权限
     */
    private Boolean commentAuth;

    /**
     * 打赏功能权限
     */
    private Boolean rewardAuth;

    /**
     * 文章发布权限
     */
    private Boolean pushArticleAuth;

    /**
     * 申请友链权限
     */
    private Boolean applyExternalAuth;

    /**
     * 用户反馈权限
     */
    private Boolean feedbackAuth;

    /**
     * 用户举报权限
     */
    private Boolean reportAuth;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
