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
 * 定时任务日志记录表
 * @TableName t_scheduled_log
 */
@TableName(value ="t_scheduled_log")
@Data
public class ScheduledLog implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 执行状态,1成功,0失败
     */
    private Boolean executeStatus;

    /**
     * 描述
     */
    private String executeDesc;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
