package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.Order;
import com.cxs.service.OrderService;
import com.cxs.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【t_order(交易订单)】的数据库操作Service实现
* @createDate 2024-05-21 10:31:08
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




