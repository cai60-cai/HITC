package com.cxs.vo.admin.report;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.vo.admin.article.AdminArticleInfoVO;
import com.cxs.vo.admin.comment.AdminCommentVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class AdminGetFeedBackInfoVO {

    private Integer reportId;

    /**
     * 举报类型
     */
    private String reportType;

    /**
     * 举报描述
     */
    private String reportContent;

    /**
     * 举报图片
     */
    private List<String> reportImages;

    /**
     * 状态,0、待处理，1、已处理
     */
    private Integer reportStatus;

    /**
     * 举报对象,1、资源博客,2、评论
     */
    private Integer reportTarget;

    /**
     * 举报对象id,report_target=1时为articleId,report_target=2时为commentId
     */
    private Integer reportTargetId;

    /**
     * 举报人id
     */
    private String reportUserId;

    /**
     * 举报时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportTime;

    /**
     * 举报处理描述
     */
    private String reportHandleDesc;

    /**
     * 处理人id
     */
    private String reportHandleAdmin;

    /**
     * 举报处理结果
     */
    private String reportHandleResult;

    /**
     * 举报处理时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportHandleTime;


    private AdminArticleInfoVO articleInfoVO;

    private AdminCommentVO commentVO;
}
