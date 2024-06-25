package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 积分交易订单表
 * @TableName t_point_trade_order
 */
@TableName(value ="t_point_trade_order")
@Data
public class PointTradeOrder implements Serializable {
    /**
     * 订单id
     */
    @TableId
    private String orderTradeNo;

    /**
     * 下单用户
     */
    private String orderUser;

    /**
     * 积分充值类型id
     */
    private Integer pointTypeId;

    /**
     * 0、未付款, 1、已付款
     */
    private Integer orderStatus;

    /**
     * 订单描述
     */
    private String orderDesc;

    private BigDecimal orderMoney;

    /**
     * 交易时间
     */
    private LocalDateTime orderTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
