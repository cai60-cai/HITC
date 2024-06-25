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
 * 积分交易流水
 * @TableName t_point_trade_flow
 */
@TableName(value ="t_point_trade_flow")
@Data
public class PointTradeFlow implements Serializable {
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
     * 变动积分值
     */
    private Integer point;

    /**
     * 积分方向,1增加、0扣除
     */
    private Integer pointType;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 交易描述
     */
    private String tradeDesc;

    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}