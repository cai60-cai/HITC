package com.cxs.vo.admin.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.vo.admin.AdminUserVO;
import com.cxs.vo.admin.user.AdminUserViewVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GetPointTradeOrderListVO {

    private String orderTradeNo;

    /**
     * 下单用户
     */
    private AdminUserViewVO orderUser;

    /**
     * 积分充值类型
     */
    private String pointTypeDesc;

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

}
