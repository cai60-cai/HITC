package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.nav.CreateOrUpdateNavLinkDTO;
import com.cxs.dto.admin.nav.NavLinkOrderMoveOrToppingOrDelDTO;
import com.cxs.model.NavLink;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface NavLinkService extends IService<NavLink> {

    /**
     * 获取navlink列表
     * @param result
     */
    void getLinkList(BaseResult result);

    /**
     * 新增navlink
     * @param dto
     * @param request
     * @param result
     */
    void createNavLink(CreateOrUpdateNavLinkDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改navlink
     * @param dto
     * @param request
     * @param result
     */
    void updateNavLink(CreateOrUpdateNavLinkDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * navlink顺序上移、下移、置顶、删除
     * @param dto
     * @param request
     * @param result
     */
    void navLinkOrderMoveOrToppingOrDel(NavLinkOrderMoveOrToppingOrDelDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 管理员获取实时链接数据
     * @param result
     */
    void getRealtimeLinkList(BaseResult result);
}
