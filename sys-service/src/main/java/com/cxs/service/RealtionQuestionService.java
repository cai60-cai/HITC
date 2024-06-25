package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.question.AdminGetQuestionListDTO;
import com.cxs.dto.admin.question.SaveOrUpdateQuestionDTO;
import com.cxs.model.RealtionQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author DELL
* @description 针对表【t_realtion_question(问题回答表)】的数据库操作Service
* @createDate 2024-05-23 19:03:10
*/
public interface RealtionQuestionService extends IService<RealtionQuestion> {

    /**
     * 获取问题列表
     * @param result
     * @param request
     */
    void questionList(BaseResult result, HttpServletRequest request);

    /**
     * 获取问题详情
     * @param id
     * @param result
     * @param request
     */
    void questionInfo(Integer id, BaseResult result, HttpServletRequest request);

    /**
     * 新增问题
     * @param dto
     * @param request
     * @param result
     */
    void saveQuestion(SaveOrUpdateQuestionDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改问题
     * @param dto
     * @param request
     * @param result
     */
    void updateQuestion(SaveOrUpdateQuestionDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除问题
     * @param id
     * @param request
     * @param result
     */
    void removeQuestion(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * admin获取问题列表
     * @param dto
     * @param request
     * @param result
     */
    void getQuestionList(AdminGetQuestionListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * admin获取问题详情
     * @param id
     * @param request
     * @param result
     */
    void getQuestionInfo(Integer id, HttpServletRequest request, BaseResult result);
}
