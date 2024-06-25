package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 定时任务配置表
 * @TableName t_scheduled
 */
@TableName(value ="t_scheduled")
@Data
public class Scheduled implements Serializable {
    /**
     * 任务id
     */
    @TableId(type = IdType.AUTO)
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}