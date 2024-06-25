package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.log.GetLogListDTO;
import com.cxs.model.LogInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface LogInfoService extends IService<LogInfo> {

    /**
     * 查询日志
     * @param dto
     * @param request
     * @param result
     */
    void getLogList(GetLogListDTO dto, HttpServletRequest request, BaseResult result);
}
