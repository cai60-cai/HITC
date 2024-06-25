package com.cxs.mapper;

import com.cxs.bo.OrderRangeCountBo;
import com.cxs.model.PointTradeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_point_trade_order(积分交易订单表)】的数据库操作Mapper
* @createDate 2024-05-27 19:56:03
* @Entity com.cxs.model.PointTradeOrder
*/
public interface PointTradeOrderMapper extends BaseMapper<PointTradeOrder> {

    List<OrderRangeCountBo> selectRangeCreateTimeCountInfo(@Param("num") Integer num);
}




