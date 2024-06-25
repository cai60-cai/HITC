package com.cxs.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cxs.config.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class AlipayUtil {

    @Autowired
    private CommonConfig commonConfig;

    /**
     * 验签，是否正确
     */
    public boolean checkSign(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>();
        requestMap.forEach((key, values) -> {
            String strs = "";
            for(String value : values) {
                strs = strs + value;
            }
            System.out.println(key +"===>"+strs);
            paramsMap.put(key, strs);
        });
        System.out.println();
        //调用SDK验证签名
        try {
            return  AlipaySignature.rsaCheckV1(paramsMap, commonConfig.getAlipayPublicKey(), commonConfig.getCharset(), commonConfig.getSignType());
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("*********************验签失败********************");
            return false;
        }

    }
}
