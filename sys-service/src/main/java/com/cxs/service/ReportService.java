package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.report.AdminGetReportListDTO;
import com.cxs.dto.admin.report.HandleReportDTO;
import com.cxs.dto.report.AddReportDTO;
import com.cxs.model.Report;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author lenovo
* @description 针对表【t_report(金币充值类型)】的数据库操作Service
* @createDate 2024-05-12 14:08:44
*/
public interface ReportService extends IService<Report> {

    /**
     * 新增用户举报
     * @param dto
     * @param request
     * @param result
     */
    void addReport(AddReportDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户删除举报
     * @param id
     * @param request
     * @param result
     */
    void delReport(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取用户举报列表
     * @param dto
     * @param request
     * @param result
     */
    void adminGetReportList(AdminGetReportListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员处理举报
     * @param dto
     * @param request
     * @param result
     */
    void adminHandleReport(HandleReportDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员删除举报
     * @param id
     * @param request
     * @param result
     */
    void adminRemoveReport(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取举报详情
     * @param id
     * @param request
     * @param result
     */
    void adminGetReportInfo(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取举报处理方式
     * @param handleType
     * @param request
     * @param result
     */
    void adminGetHandleType(Integer handleType, HttpServletRequest request, BaseResult result);
}
