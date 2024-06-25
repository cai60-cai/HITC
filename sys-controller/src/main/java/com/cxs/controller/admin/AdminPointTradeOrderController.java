package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.point.GetPointTradeOrderListDTO;
import com.cxs.dto.admin.user.GetUserListDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.PointRechargeTypeService;
import com.cxs.service.PointTradeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/tradeOrder")
@Api(tags = "管理员积分订单控制器")
public class AdminPointTradeOrderController {

    @Autowired
    private PointTradeOrderService pointTradeOrderService;

    @PostMapping("/getPointTradeOrderList")
    @ApiOperation("获取积分交易订单列表处理器")
    public BaseResult adminGetPointTradeOrderList(@RequestBody @Validated GetPointTradeOrderListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!adminGetPointTradeOrderListParamHandle(dto, result)) {
            return result;
        }
        pointTradeOrderService.adminGetPointTradeOrderList(dto, request, result);
        return result;
    }

    /**
     * 获取积分交易订单列表入参校验
     * @param dto
     * @param result
     * @return
     */
    private boolean adminGetPointTradeOrderListParamHandle(GetPointTradeOrderListDTO dto, BaseResult result) {
        if (dto.getPageNum() < 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode())
                    .setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",pageNum不能为负值");
            return false;
        }
        if (dto.getPageSize() < 0) {
            result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode())
                    .setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",pageSize不能为负值");
            return false;
        }
        return true;
    }
}
