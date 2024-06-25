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
 * 金币充值类型
 * @TableName t_report
 */
@TableName(value ="t_report")
@Data
public class Report implements Serializable {
    /**
     * report_id
     */
    @TableId(type = IdType.AUTO)
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
    private String reportImages;

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
    private LocalDateTime reportHandleTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}