package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 标签表
 * @TableName t_tag
 */
@TableName(value ="t_tag")
@Data
public class Tag implements Serializable {
    /**
     * 标签id
     */
    @TableId(type = IdType.AUTO)
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}