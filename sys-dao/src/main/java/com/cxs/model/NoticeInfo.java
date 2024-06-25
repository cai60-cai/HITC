package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 公告信息表
 * @TableName t_notice_info
 */
@TableName(value ="t_notice_info")
@Data
public class NoticeInfo implements Serializable {
    /**
     * 公告id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 是否首页轮询推送
     */
    private Integer isPush;

    /**
     * 发布人用户id
     */
    private String publishUserId;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}