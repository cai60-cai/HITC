package com.cxs.vo.user;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class UserReportInfoVO {
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

    private JSONObject reportTargetObj;

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
     * 举报处理结果
     */
    private String reportHandleResult;

    /**
     * 举报处理时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportHandleTime;
}
