package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.client.req.AliReturnPayBean;
import com.cxs.dto.admin.point.SaveOrUpdatePointRechargeTypeDTO;
import com.cxs.dto.point.PointTradeDTO;
import com.cxs.model.PointRechargeType;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author lenovo
* @description 针对表【t_gold_recharge_type(金币充值类型)】的数据库操作Service
* @createDate 2024-05-21 09:39:18
*/
public interface PointRechargeTypeService extends IService<PointRechargeType> {

    /**
     * 获取积分充值类型列表
     * @param request
     * @param result
     */
    void getPointRechargeTypeList(HttpServletRequest request, BaseResult result);

    /**
     * 积分交易
     * @param dto
     * @param request
     * @param result
     */
    void trade(PointTradeDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 支付宝回调
     * @param request
     * @param response
     * @param returnPay
     */
    void call(HttpServletRequest request, HttpServletResponse response, AliReturnPayBean returnPay);

    /**
     * 管理员获取积分充值类型
     * @param request
     * @param result
     */
    void adminGetPointRechargeTypeList(HttpServletRequest request, BaseResult result);

    /**
     * 新增积分充值类型
     * @param dto
     * @param request
     * @param result
     */
    void adminSavePointRechargeType(SaveOrUpdatePointRechargeTypeDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改积分充值类型
     * @param dto
     * @param request
     * @param result
     */
    void adminUpdatePointRechargeType(SaveOrUpdatePointRechargeTypeDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除积分充值类型
     * @param id
     * @param request
     * @param result
     */
    void removePointRechargeType(Integer id, HttpServletRequest request, BaseResult result);
}
