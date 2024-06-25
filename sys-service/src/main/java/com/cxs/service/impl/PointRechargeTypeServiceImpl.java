package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.client.PayClient;
import com.cxs.client.req.AliReturnPayBean;
import com.cxs.client.req.PayReq;
import com.cxs.constant.CommonContent;
import com.cxs.dto.admin.point.SaveOrUpdatePointRechargeTypeDTO;
import com.cxs.dto.point.PointTradeDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.PointTradeFlowMapper;
import com.cxs.mapper.PointTradeOrderMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.model.PointRechargeType;
import com.cxs.model.PointTradeFlow;
import com.cxs.model.PointTradeOrder;
import com.cxs.service.PointRechargeTypeService;
import com.cxs.mapper.PointRechargeTypeMapper;
import com.cxs.service.UserService;
import com.cxs.utils.AlipayUtil;
import com.cxs.utils.RedisUtil;
import com.cxs.vo.admin.recharge.RechargeTypeListVO;
import com.cxs.vo.recharge.RechargeTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.cxs.constant.CachePrefixContent.POINT_TRADE_PREFIX;

/**
* @author lenovo
* @description 针对表【t_gold_recharge_type(金币充值类型)】的数据库操作Service实现
* @createDate 2024-05-21 09:39:18
*/
@Slf4j
@Service
public class PointRechargeTypeServiceImpl extends ServiceImpl<PointRechargeTypeMapper, PointRechargeType> implements PointRechargeTypeService {

    @Autowired
    private PointRechargeTypeMapper pointRechargeTypeMapper;

    @Autowired
    private AlipayUtil alipayUtil;

    @Autowired
    private PayClient payClient;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketPay webSocketPay;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointTradeFlowMapper pointTradeFlowMapper;

    @Autowired
    private PointTradeOrderMapper pointTradeOrderMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void getPointRechargeTypeList(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<PointRechargeType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PointRechargeType::getShows, Boolean.TRUE).orderByAsc(PointRechargeType::getGold);
            List<PointRechargeType> pointRechargeTypes = pointRechargeTypeMapper.selectList(queryWrapper);
            List<RechargeTypeVO> voList = CollectionUtils.isEmpty(pointRechargeTypes) ? new ArrayList<>(0) :
                    pointRechargeTypes.stream().map(r -> {
                        RechargeTypeVO vo = new RechargeTypeVO();
                        BeanUtils.copyProperties(r, vo);
                        Double discount = r.getDiscount();
                        if (ObjectUtils.isEmpty(discount)) {
                            vo.setMoney(r.getMoney().toString());
                        } else {
                            vo.setMoney(r.getMoney().multiply(new BigDecimal(discount.toString())).divide(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        }
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取充值类型失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取金币充值类型接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    @Transactional
    public void trade(PointTradeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String header = request.getHeader(CommonContent.ACCESS_TOKEN);
            UserSubject userByToken = userService.getUserByToken(request);
            PointRechargeType pointRechargeType = pointRechargeTypeMapper.selectById(dto.getId());
            if (ObjectUtils.isEmpty(pointRechargeType) || !pointRechargeType.getShows()) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该商品不存在");
            } else {
                String orderId = "XBBK" + userByToken.getId() + System.currentTimeMillis();
                PayReq req = new PayReq();
                req.setOutTradeNo(orderId);
                Double discount = pointRechargeType.getDiscount();
                String money = null;
                if (ObjectUtils.isEmpty(discount)) {
                    money = pointRechargeType.getMoney().toString();
                } else {
                    money = pointRechargeType.getMoney().multiply(new BigDecimal(discount.toString())).divide(new BigDecimal("10")).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                }
                req.setTotalAmount(money);
                req.setSubject("用户购买积分*" + pointRechargeType.getGold());
                JSONObject body = new JSONObject();
                body.put("token", header);
                body.put("orderId", orderId);
                body.put("userId", userByToken.getId());
                body.put("productId", pointRechargeType.getId());
                req.setBody(JSON.toJSONString(body));
                String pay = payClient.pay(req);
                result.setData(pay);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("积分充值失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【积分充值接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, dto, result);
        }
    }

    @Override
    public void call(HttpServletRequest request, HttpServletResponse response, AliReturnPayBean returnPay) {
        long startTime = System.currentTimeMillis();
        try {
            response.setContentType("type=text/html;charset=UTF-8");
            if (!alipayUtil.checkSign(request)) {
                log.error("签名验证失败");
                return;
            }
            if (returnPay == null) {
                log.error("支付宝的returnPay返回为空");
                return;
            }
            log.info("支付宝的returnPay" + returnPay.toString());
            //表示支付成功状态下的操作
            if (returnPay.getTrade_status().equals("TRADE_SUCCESS")) {
                //业务逻辑处理 ,webSocket在下面会有介绍配置
                String body = returnPay.getBody();
                if (body != null) {
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    String token = jsonObject.getString("token");
                    if (StringUtils.hasLength(token)) {
                        webSocketPay.sendMessage(token, "true");
                    } else {
                        webSocketPay.sendMessage(null, "true");
                    }
                    String userId = jsonObject.getString("userId");
                    Integer id = jsonObject.getInteger("productId");
                    String orderId = jsonObject.getString("orderId");
                    PointRechargeType rechargeType = pointRechargeTypeMapper.selectById(id);
                    Integer res = userMapper.updateOperaPointById(userId, rechargeType.getGold(), CommonContent.POINT_INCR);
                    if (res > 0) {
                        // 记录订单
                        PointTradeOrder order = new PointTradeOrder();
                        order.setOrderTradeNo(returnPay.getOut_trade_no());
                        order.setOrderUser(userId);
                        order.setOrderMoney(rechargeType.getMoney());
                        order.setPointTypeId(rechargeType.getId());
                        order.setOrderStatus(1);
                        order.setOrderTime(LocalDateTime.now());
                        pointTradeOrderMapper.insert(order);
                        // 流水
                        PointTradeFlow tradeFlow = new PointTradeFlow();
                        tradeFlow.setUserId(userId);
                        tradeFlow.setPoint(rechargeType.getGold());
                        tradeFlow.setTradeTime(LocalDateTime.now());
                        tradeFlow.setPointType(CommonContent.POINT_INCR);
                        tradeFlow.setTradeDesc("积分购买");
                        pointTradeFlowMapper.insert(tradeFlow);
                        return;
                    }
                }
            }

            String body = returnPay.getBody();
            if (body != null) {
                JSONObject jsonObject = JSONObject.parseObject(body);
                String token = jsonObject.getString("token");
                if (StringUtils.hasLength(token)) {
                    webSocketPay.sendMessage(token, "false");
                } else {
                    webSocketPay.sendMessage(null, "false");
                }
                String userId = jsonObject.getString("userId");
                Integer id = jsonObject.getInteger("productId");
                String orderId = jsonObject.getString("orderId");
                PointRechargeType rechargeType = pointRechargeTypeMapper.selectById(id);

                // 记录订单
                PointTradeOrder order = new PointTradeOrder();
                order.setOrderTradeNo(returnPay.getOut_trade_no());
                order.setOrderUser(userId);
                order.setOrderMoney(rechargeType.getMoney());
                order.setPointTypeId(rechargeType.getId());
                order.setOrderStatus(0);
                order.setOrderTime(LocalDateTime.now());
                pointTradeOrderMapper.insert(order);
                redisUtil.set(POINT_TRADE_PREFIX + orderId, orderId, 30L, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("积分充值回调执行失败：" + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【积分充值回调接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, "", "");
        }
    }

    @Override
    public void adminGetPointRechargeTypeList(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<PointRechargeType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(PointRechargeType::getGold);
            List<PointRechargeType> pointRechargeTypes = pointRechargeTypeMapper.selectList(queryWrapper);
            List<RechargeTypeListVO> voList = CollectionUtils.isEmpty(pointRechargeTypes) ? new ArrayList<>(0) :
                    pointRechargeTypes.stream().map(r -> {
                        RechargeTypeListVO vo = new RechargeTypeListVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员获取充值类型失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取金币充值类型接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void adminSavePointRechargeType(SaveOrUpdatePointRechargeTypeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            PointRechargeType type = new PointRechargeType();
            BeanUtils.copyProperties(dto, type);
            int insert = pointRechargeTypeMapper.insert(type);
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            } else {
                RechargeTypeListVO vo = new RechargeTypeListVO();
                BeanUtils.copyProperties(type, vo);
                result.setData(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员新增类型失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员新增类型接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, "", result);
        }
    }

    @Override
    public void adminUpdatePointRechargeType(SaveOrUpdatePointRechargeTypeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            PointRechargeType type = new PointRechargeType();
            BeanUtils.copyProperties(dto, type);
            int update = pointRechargeTypeMapper.updateById(type);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员修改类型失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员修改类型接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, "", result);
        }
    }

    @Override
    public void removePointRechargeType(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            pointRechargeTypeMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员删除类型失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员删除类型接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }
}




