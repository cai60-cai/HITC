package com.cxs.runner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cxs.base.BaseResult;
import com.cxs.mapper.PointTradeOrderMapper;
import com.cxs.model.PointTradeOrder;
import com.cxs.runner.AbstractScheduledTaskJob;
import com.cxs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.cxs.constant.CachePrefixContent.POINT_TRADE_PREFIX;

  
@Slf4j
@Service
public class PointOrderStatusTask extends AbstractScheduledTaskJob implements BeanNameAware {

    @Autowired
    private PointTradeOrderMapper pointTradeOrderMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void execute(BaseResult result) {
        Integer count = 0;
        LambdaQueryWrapper<PointTradeOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PointTradeOrder::getOrderStatus, 0);
        List<PointTradeOrder> tradeOrders = pointTradeOrderMapper.selectList(lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(tradeOrders)) {
            for (PointTradeOrder tradeOrder : tradeOrders) {
                String str = redisUtil.getString(POINT_TRADE_PREFIX + tradeOrder.getOrderTradeNo());
                if (!StringUtils.hasLength(str)) {
                    // 修改状态为已超时
                    PointTradeOrder order = new PointTradeOrder();
                    order.setOrderTradeNo(tradeOrder.getOrderTradeNo());
                    order.setOrderStatus(2);
                    int update = pointTradeOrderMapper.updateById(order);
                    if (update > 0) {
                        count++;
                    }
                }
            }
        }
        result.setMsg("订单状态任务更新条数:" + count);
    }


    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
