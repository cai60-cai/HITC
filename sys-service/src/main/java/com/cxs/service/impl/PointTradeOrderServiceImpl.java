package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.point.GetPointTradeOrderListDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.PointRechargeTypeMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.model.Article;
import com.cxs.model.PointRechargeType;
import com.cxs.model.PointTradeOrder;
import com.cxs.model.TechnologyType;
import com.cxs.model.User;
import com.cxs.service.PointRechargeTypeService;
import com.cxs.service.PointTradeOrderService;
import com.cxs.mapper.PointTradeOrderMapper;
import com.cxs.vo.admin.article.ArticleReviewedListVO;
import com.cxs.vo.admin.order.GetPointTradeOrderListVO;
import com.cxs.vo.admin.user.AdminUserViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author DELL
* @description 针对表【t_point_trade_order(积分交易订单表)】的数据库操作Service实现
* @createDate 2024-05-27 19:56:03
*/
@Slf4j
@Service
public class PointTradeOrderServiceImpl extends ServiceImpl<PointTradeOrderMapper, PointTradeOrder>
    implements PointTradeOrderService{

    @Autowired
    private PointTradeOrderMapper pointTradeOrderMapper;

    @Autowired
    private PointRechargeTypeMapper pointRechargeTypeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void adminGetPointTradeOrderList(GetPointTradeOrderListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            BasePageBean pageBean = new BasePageBean();
            Page<PointTradeOrder> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<PointTradeOrder> queryWrapper = new LambdaQueryWrapper<>();
            if (!ObjectUtils.isEmpty(dto.getOrderStstus())) {
                queryWrapper.eq(PointTradeOrder::getOrderStatus, dto.getOrderStstus());
            }
            if (StringUtils.hasLength(dto.getOrderUser())) {
                queryWrapper.eq(PointTradeOrder::getOrderUser, dto.getOrderUser());
            }
            queryWrapper.orderByDesc(PointTradeOrder::getOrderTime);
            IPage<PointTradeOrder> iPage = pointTradeOrderMapper.selectPage(page, queryWrapper);
            List<PointTradeOrder> pointTradeOrderList = iPage.getRecords();

            List<GetPointTradeOrderListVO> voList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointTradeOrderList)) {
                List<PointRechargeType> pointRechargeTypes = pointRechargeTypeMapper.selectList(null);
                Map<Integer, PointRechargeType> pointRechargeTypeMap = CollectionUtils.isEmpty(pointRechargeTypes) ? new HashMap<>() : pointRechargeTypes.stream().collect(Collectors.toMap(PointRechargeType::getId, Function.identity(), (o1, o2) -> o1));

                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.select(User::getUserId, User::getAvatar, User::getUserName, User::getNickName);
                List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
                Map<String, User> userMap = CollectionUtils.isEmpty(userList) ? new HashMap<>() : userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (o1, o2) -> o1));
                voList = pointTradeOrderList.stream().map(r -> {
                    GetPointTradeOrderListVO vo = new GetPointTradeOrderListVO();
                    BeanUtils.copyProperties(r, vo);
                    PointRechargeType type = pointRechargeTypeMap.get(r.getPointTypeId());
                    if (!ObjectUtils.isEmpty(type)) {
                        vo.setPointTypeDesc(type.getTypeDesc());
                    }
                    User user = userMap.get(r.getOrderUser());
                    if (!ObjectUtils.isEmpty(user)) {
                        AdminUserViewVO userViewVO = new AdminUserViewVO();
                        BeanUtils.copyProperties(user, userViewVO);
                        vo.setOrderUser(userViewVO);
                    }
                    return vo;
                }).collect(Collectors.toList());
            }
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("管理员获取订单列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取订单列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }
}




