package com.cxs.client.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


@Data
public class PayReq {

    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "total_amount")
    private String totalAmount;
    private String subject;
    private String body;
    // @JSONField(name = "product_code")
    // private String productCode = "FAST_INSTANT_TRADE_PAY";
}
