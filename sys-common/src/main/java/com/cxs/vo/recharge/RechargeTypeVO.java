package com.cxs.vo.recharge;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class RechargeTypeVO {

    private Integer id;
    /**
     * 金币数量
     */
    private Integer gold;
    /**
     * money
     */
    private String money;
    /**
     * 描述
     */
    private String typeDesc;
}
