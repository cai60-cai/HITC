package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.task.GetTaskLogListDTO;
import com.cxs.dto.admin.task.SaveOrUpdateTaskDTO;
import com.cxs.dto.admin.task.TaskHandleDTO;
import com.cxs.model.Scheduled;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface ScheduledService extends IService<Scheduled> {
    /**
     * 获取任务列表
     * @param keyword
     * @param request
     * @param result
     */
    void getTaskList(String keyword, HttpServletRequest request, BaseResult result);

    /**
     * 新增或修改任务
     * @param dto
     * @param request
     * @param result
     */
    void saveOrUpdateTask(SaveOrUpdateTaskDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除任务处理器
     * @param taskId
     * @param request
     * @param result
     */
    void removeTask(Integer taskId, HttpServletRequest request, BaseResult result);

    /**
     * 定时任务操作
     * @param dto
     * @param request
     * @param result
     */
    void taskOperaHandle(TaskHandleDTO dto, HttpServletRequest request, BaseResult result);


    /**
     * 执行一次任务
     * @param taskId
     * @param request
     * @param result
     */
    void executeOnce(Integer taskId, HttpServletRequest request, BaseResult result);

    /**
     * 获取定时任务日志
     * @param dto
     * @param request
     * @param result
     */
    void getTaskLog(GetTaskLogListDTO dto, HttpServletRequest request, BaseResult result);
}
