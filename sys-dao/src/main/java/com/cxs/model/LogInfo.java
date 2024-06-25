package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 日志表
 * @TableName t_log_info
 */
@TableName(value ="t_log_info")
@Data
public class LogInfo implements Serializable {
    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作结果 1、成功 2、失败
     */
    private Integer operaResult;

    /**
     * 操作方法名
     */
    private String operaMethod;

    /**
     * 入参
     */
    private String param;

    /**
     * 出参
     */
    private String response;

    /**
     * 操作说明
     */
    private String operaDesc;

    /**
     * 失败原因描述
     */
    private String operaErrorWhy;

    /**
     * 操作人用户id
     */
    private String operaUserId;

    /**
     * 操作时间
     */
    private LocalDateTime operaTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}