package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.client.req.AliReturnPayBean;
import com.cxs.service.impl.WebSocketPay;
import com.cxs.dto.point.PointTradeDTO;
import com.cxs.service.PointRechargeTypeService;
import com.cxs.utils.AlipayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/rechangeType")
@Api(tags = "积分充值控制器")
public class PointRechangeTypeController {

    @Autowired
    private PointRechargeTypeService pointRechargeTypeService;

    @Autowired
    private AlipayUtil alipayUtil;

    @Autowired
    private WebSocketPay webSocketPay;

    @PostMapping("/trade")
    @ApiOperation("积分交易处理器")
    public BaseResult trade(@RequestBody PointTradeDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        pointRechargeTypeService.trade(dto, request, result);
        return result;
    }

    /**
     * 支付宝回调函数
     * @param request
     * @param response
     * @param returnPay
     * @throws IOException
     */
    @RequestMapping("/call")
    public void call(HttpServletRequest request, HttpServletResponse response, AliReturnPayBean returnPay) throws IOException {
        pointRechargeTypeService.call(request, response, returnPay);
    }
}
