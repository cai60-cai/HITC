package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户权限表
 * @TableName t_user_auth
 */
@TableName(value ="t_user_auth")
@Data
public class UserAuth implements Serializable {
    /**
     * 用户id
     */
    @TableId
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
     * 举报权限
     */
    private Boolean reportAuth;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}