package com.cxs.client.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.cxs.client.PayClient;
import com.cxs.client.req.Pay1Req;
import com.cxs.client.req.PayReq;
import com.cxs.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PayClientImpl implements PayClient {

    @Autowired
    private CommonConfig commonConfig;
    @Override
    public String pay(PayReq req) {
        String result = null;
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    commonConfig.getGatewayUrl(),
                    commonConfig.getAppId(),
                    commonConfig.getMerchantPrivateKey(),
                    "json",
                    commonConfig.getCharset(),
                    commonConfig.getAlipayPublicKey(),
                    commonConfig.getSignType());
            //设置请求参数
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(commonConfig.getNotifyUrl());
            request.setBizContent(JSON.toJSONString(req));
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                result = response.getQrCode();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("[支付宝支付接口]: 支付失败,入参:{}, 出参:{}", req, result);
        }
        return result;
    }
}
