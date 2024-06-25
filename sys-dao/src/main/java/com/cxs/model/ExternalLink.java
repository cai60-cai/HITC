package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 外部链接表
 * @TableName t_external_link
 */
@TableName(value ="t_external_link")
@Data
public class ExternalLink implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer externalLinkId;

    /**
     * 链接名称
     */
    private String externalLinkName;

    /**
     * 链接地址
     */
    private String externalLinkLink;

    /**
     * 链接图标
     */
    private String externalLinkIcon;

    /**
     * 链接打开方式
     */
    private String externalLinkBlank;

    /**
     * 状态,0、待审核1、已通过
     */
    private Integer externalLinkStatus;

    /**
     * 所属用户
     */
    private String externalLinkUserId;

    /**
     * 备注
     */
    private String externalLinkDesc;

    /**
     * 链接创建时间
     */
    private LocalDateTime createTime;

    /**
     * 链接修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}