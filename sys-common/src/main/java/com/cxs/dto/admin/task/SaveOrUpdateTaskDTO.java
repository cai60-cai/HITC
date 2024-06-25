package com.cxs.dto.admin.task;

import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class SaveOrUpdateTaskDTO {

    private Integer taskId;

    @NotBlank(message = "taskKey为必传项")
    private String taskKey;

    /**
     * 任务名称
     */
    @NotBlank(message = "taskName为必传项")
    private String taskName;

    /**
     * 任务描述
     */
    @NotBlank(message = "taskDesc为必传项")
    private String taskDesc;

    /**
     * 任务表达式
     */
    @NotBlank(message = "taskCron为必传项")
    private String taskCron;

    /**
     * 状态(0.禁用; 1.启用)
     */
    @NotNull(message = "taskStatus为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "taskStatus范围{0,1}")
    private Integer taskStatus;
}
