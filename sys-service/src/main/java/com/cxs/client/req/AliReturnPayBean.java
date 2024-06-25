package com.cxs.client.req;



import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝回调参数
 */
@Data
public class AliReturnPayBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 开发者的app_id
     */
    private String app_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 签名
     */
    private String sign;

    /**
     * 交易状态
     */
    private String trade_status;

    /**
     * 支付宝交易号
     */
    private String trade_no;

    /**
     * 交易的金额
     */
    private String total_amount;

    private String body;
}
