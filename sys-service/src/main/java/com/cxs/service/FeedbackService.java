package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.feedback.GetFeedBackListDTO;
import com.cxs.dto.admin.feedback.HandleFeedBackDTO;
import com.cxs.dto.feedback.AddFeedbackDTO;
import com.cxs.model.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author lenovo
* @description 针对表【t_feedback(用户反馈表)】的数据库操作Service
* @createDate 2024-05-18 10:43:37
*/
public interface FeedbackService extends IService<Feedback> {

    /**
     * 用户添加反馈
     * @param dto
     * @param request
     * @param result
     */
    void addFeedback(AddFeedbackDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取用户反馈列表
     * @param dto
     * @param request
     * @param result
     */
    void getFeedBackList(GetFeedBackListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员处理反馈
     * @param dto
     * @param request
     * @param result
     */
    void handleFeedBack(HandleFeedBackDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户删除反馈
     * @param id
     * @param request
     * @param result
     */
    void delFeedback(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取用户反馈详情
     * @param id
     * @param request
     * @param result
     */
    void adminGetFeedBackInfo(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 管理员删除反馈
     * @param id
     * @param request
     * @param result
     */
    void adminDelFeedback(Integer id, HttpServletRequest request, BaseResult result);
}
