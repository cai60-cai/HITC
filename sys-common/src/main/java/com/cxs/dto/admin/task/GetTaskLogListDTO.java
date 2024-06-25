package com.cxs.dto.admin.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import java.time.LocalDateTime;

  
@Data
public class GetTaskLogListDTO extends BaseRequest {

    private Integer taskId;
    /**
     * 操作结果 1、成功 2、失败
     */
    private Boolean executeStatus;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
