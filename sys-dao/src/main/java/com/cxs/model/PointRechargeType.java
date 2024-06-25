package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 积分充值类型
 * @TableName t_gold_recharge_type
 */
@TableName(value ="t_point_recharge_type")
@Data
public class PointRechargeType implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 积分数量
     */
    private Integer gold;

    /**
     * money
     */
    private BigDecimal money;

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 是否展示
     */
    private Boolean shows;

    /**
     * 描述
     */
    private String typeDesc;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}