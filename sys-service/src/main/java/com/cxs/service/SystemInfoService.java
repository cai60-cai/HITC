package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.model.SystemInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface SystemInfoService extends IService<SystemInfo> {
    /**
     * 获取详情
     * @param request
     * @param result
     */
    void getSystemInfoInfo(HttpServletRequest request, BaseResult result);

    /**
     * 修改
     * @param info
     * @param request
     * @param result
     */
    void updateSystemInfo(SystemInfo info, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取首页看板统计
     * @param request
     * @param result
     */
    void getSystemStatisticsInfo(HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取范围统计
     * @param request
     * @param result
     */
    void getSystemRangeStatisticsInfo(HttpServletRequest request, BaseResult result);
}
