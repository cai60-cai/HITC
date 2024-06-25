package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.point.GetPointTradeOrderListDTO;
import com.cxs.model.PointTradeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author DELL
* @description 针对表【t_point_trade_order(积分交易订单表)】的数据库操作Service
* @createDate 2024-05-27 19:56:03
*/
public interface PointTradeOrderService extends IService<PointTradeOrder> {

    /**
     * 管理员获取积分交易订单列表
     * @param request
     * @param result
     */
    void adminGetPointTradeOrderList(GetPointTradeOrderListDTO dto, HttpServletRequest request, BaseResult result);
}
