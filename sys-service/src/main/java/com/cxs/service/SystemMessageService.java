package com.cxs.service;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.dto.message.ReadMessageDTO;
import com.cxs.model.SystemMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxs.vo.message.SysMessageVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author DELL
* @description 针对表【t_system_message(系统通知表)】的数据库操作Service
* @createDate 2024-06-10 15:54:24
*/
public interface SystemMessageService extends IService<SystemMessage> {

    /**
     * 获取消息通知列表
     * @param dto
     * @param request
     * @param result
     */
    void getSystemMessage(BaseRequest dto, HttpServletRequest request, BaseResult result);

    /**
     * 设置消息已读
     * @param dto
     * @param request
     * @param result
     */
    void readSysMessage(ReadMessageDTO dto, HttpServletRequest request, BaseResult result);


    /**
     * 保存消息
     * @param data
     * @param send
     * @param userId
     * @param result
     */
    void saveSysMessage(SystemMessage data, Boolean send,  String userId, BaseResult result);
}
