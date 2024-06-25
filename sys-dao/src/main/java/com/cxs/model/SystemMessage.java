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
 * 系统通知表
 * @TableName t_system_message
 */
@TableName(value ="t_system_message")
@Data
public class SystemMessage implements Serializable {
    /**
     * 消息id、主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 通知用户id、为空则为全部通知
     */
    private String receiveUserId;

    /**
     * 通知内容
     */
    private String messageContent;

    /**
     * 通知创建时间
     */
    private LocalDateTime createTime;

    /**
     * 已读标记
     */
    private Boolean isRead;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
