package com.cxs.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserReportListVO {
    private Integer reportId;
    /**
     * 举报类型
     */
    private String reportType;

    /**
     * 状态,0、待处理，1、已处理
     */
    private Integer reportStatus;

    /**
     * 举报对象,1、资源博客,2、评论
     */
    private Integer reportTarget;

    /**
     * 举报时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportTime;
}
