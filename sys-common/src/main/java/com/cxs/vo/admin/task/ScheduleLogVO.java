package com.cxs.vo.admin.task;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ScheduleLogVO {

    private Integer id;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 执行时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime executeTime;

    /**
     * 执行状态,1成功,0失败
     */
    private Boolean executeStatus;

    /**
     * 描述
     */
    private String executeDesc;
}
