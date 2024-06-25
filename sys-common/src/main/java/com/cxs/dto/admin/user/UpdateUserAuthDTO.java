package com.cxs.dto.admin.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class UpdateUserAuthDTO {

    @NotBlank(message = "用户id为必传项")
    private String userId;

    /**
     * 文件上传权限
     */
    @NotNull(message = "uploadAuth为必传项")
    private Boolean uploadAuth;

    /**
     * 发言权限
     */
    @NotNull(message = "commentAuth为必传项")
    private Boolean commentAuth;

    /**
     * 打赏功能权限
     */
    @NotNull(message = "rewardAuth为必传项")
    private Boolean rewardAuth;

    /**
     * 文章发布权限
     */
    @NotNull(message = "pushArticleAuth为必传项")
    private Boolean pushArticleAuth;

    /**
     * 申请友链权限
     */
    @NotNull(message = "applyExternalAuth为必传项")
    private Boolean applyExternalAuth;

    /**
     * 用户反馈权限
     */
    @NotNull(message = "feedbackAuth为必传项")
    private Boolean feedbackAuth;
}
