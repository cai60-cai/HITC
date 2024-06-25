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
 * 交易订单
 * @TableName t_order
 */
@TableName(value ="t_order")
@Data
public class Order implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}