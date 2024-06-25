package com.cxs.dto.admin.point;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 积分充值类型
 * @TableName t_gold_recharge_type
 */
@Data
public class SaveOrUpdatePointRechargeTypeDTO implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 积分数量
     */
    @NotNull(message = "gold岂能为空")
    @Min(value = 0L, message = "gold必须大于0")
    private Integer gold;

    /**
     * money
     */
    @NotNull(message = "money岂能为空")
    @Min(value = 0L, message = "money必须大于0")
    private BigDecimal money;

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 是否展示
     */
    private Boolean shows = false;

    /**
     * 描述
     */
    private String typeDesc;

    private static final long serialVersionUID = 1L;
}