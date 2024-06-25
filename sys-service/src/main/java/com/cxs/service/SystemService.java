package com.cxs.service;

import com.cxs.base.BaseResult;

import javax.servlet.http.HttpServletRequest;

public interface SystemService {
    /**
     * 获取关于本站信息
     * @param request
     * @param result
     */
    void getAboutSysInfo(HttpServletRequest request, BaseResult result);

    /**
     * 关于本站
     * @param request
     * @param result
     */
    void getSystemInfo(HttpServletRequest request, BaseResult result);
}
