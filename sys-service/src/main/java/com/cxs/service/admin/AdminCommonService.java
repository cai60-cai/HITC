package com.cxs.service.admin;

import com.cxs.base.BaseResult;


public interface AdminCommonService {
    /**
     * 加密字符串
     * @param keyword
     * @param result
     */
    void getEncryptStr(String keyword, BaseResult result);

    /**
     * 解密字符串
     * @param encryptStr
     * @param result
     */
    void getDecryptStr(String encryptStr, BaseResult result);
}
