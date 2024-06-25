package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.external.GetExternalLinkListDTO;
import com.cxs.dto.external.ApplyExternalLinkDTO;
import com.cxs.dto.external.ApproveExternalFriendLinkDTO;
import com.cxs.dto.external.SaveOrUpdateExternalFriendLinkDTO;
import com.cxs.model.ExternalLink;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface ExternalLinkService extends IService<ExternalLink> {

    /**
     * 用户获取外部链接
     * @param result
     */
    void getExternalLinkList(BaseResult result);

    /**
     * 用户申请友链
     * @param dto
     * @param request
     * @param result
     */
    void applyExternalLink(ApplyExternalLinkDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取友情链接
     * @param dto
     * @param request
     * @param result
     */
    void getExternalFriendLinkList(GetExternalLinkListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员审核友情链接
     * @param dto
     * @param request
     * @param result
     */
    void approveExternalFriendLink(ApproveExternalFriendLinkDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 创建外部友情链接
     * @param dto
     * @param request
     * @param result
     */
    void saveExternalFriendLink(SaveOrUpdateExternalFriendLinkDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改外部友情链接
     * @param dto
     * @param request
     * @param result
     */
    void updateExternalFriendLink(SaveOrUpdateExternalFriendLinkDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除
     * @param id
     * @param request
     * @param result
     */
    void deleteExternalFriendLink(Integer id, HttpServletRequest request, BaseResult result);
}
