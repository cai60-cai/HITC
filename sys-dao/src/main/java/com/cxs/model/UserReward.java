package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户打赏配置表
 * @TableName t_user_reward
 */
@TableName(value ="t_user_reward")
@Data
public class UserReward implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private String userId;

    /**
     * 微信收款码
     */
    private String weixinImg;

    /**
     * 微信边框颜色
     */
    private String weixinBorderColor;

    /**
     * 支付宝收款码
     */
    private String zhifubaoImg;

    /**
     * 支付宝边框颜色
     */
    private String zhifubaoBorderColor;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}