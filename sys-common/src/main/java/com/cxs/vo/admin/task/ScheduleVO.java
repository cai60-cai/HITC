package com.cxs.vo.admin.task;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ScheduleVO {
    private Integer taskId;

    /**
     * 任务key值（使用bean名称）
     */
    private String taskKey;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 任务表达式
     */
    private String taskCron;

    /**
     * 状态(0.禁用; 1.启用)
     */
    private Integer taskStatus;

    /**
     * 运行状态
     */
    private Boolean runStatus;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
