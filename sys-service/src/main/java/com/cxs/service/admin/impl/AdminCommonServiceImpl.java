package com.cxs.service.admin.impl;

import com.alibaba.fastjson.JSONObject;
import com.cxs.base.BaseResult;
import com.cxs.config.CommonConfig;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.admin.AdminCommonService;
import com.cxs.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class AdminCommonServiceImpl implements AdminCommonService {

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private AesUtil aesUtil;

    @Override
    public void getEncryptStr(String keyword, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            if (!StringUtils.hasLength(keyword)) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg());
            } else {
                String encrypt = aesUtil.aesEncrypt(keyword);
                result.setData(encrypt);
            }
        } catch (Exception e) {
            log.error("字符串加密失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【字符串加密接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, keyword, result);
        }
    }

    @Override
    public void getDecryptStr(String encryptStr, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            if (!StringUtils.hasLength(encryptStr)) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg());
            } else {
                String decrypt = aesUtil.aesDecrypt(encryptStr);
                result.setData(decrypt);
            }
        } catch (Exception e) {
            log.error("字符串解密失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【字符串解密接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, encryptStr, result);
        }
    }
}
