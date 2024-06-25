package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.bo.ArticleRangeCountBo;
import com.cxs.bo.OrderRangeCountBo;
import com.cxs.bo.UserLoginRangeCountBo;
import com.cxs.bo.UserRangeCountBo;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ArticleMapper;
import com.cxs.mapper.PointTradeOrderMapper;
import com.cxs.mapper.UserLoginLogMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.model.Article;
import com.cxs.model.NoticeInfo;
import com.cxs.model.PointTradeOrder;
import com.cxs.model.SystemInfo;
import com.cxs.model.User;
import com.cxs.model.UserLoginLog;
import com.cxs.service.SystemInfoService;
import com.cxs.mapper.SystemInfoMapper;
import com.cxs.utils.DateUtil;
import com.cxs.vo.admin.dashboard.GetSystemRangeStatisticsInfoVO;
import com.cxs.vo.admin.dashboard.MainCountVO;
import com.cxs.vo.main.MainSimpleNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemInfoServiceImpl extends ServiceImpl<SystemInfoMapper, SystemInfo> implements SystemInfoService{

    @Autowired
    private SystemInfoMapper systemInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private PointTradeOrderMapper pointTradeOrderMapper;

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Override
    public void getSystemInfoInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            SystemInfo systemInfo = systemInfoMapper.selectOne(null);
            result.setData(systemInfo);
        } catch (Exception e) {
            log.error("管理员获取本站信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取本站信息接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void updateSystemInfo(SystemInfo info, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            if (info.getId() == null) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",入参错误");
            } else {
                int update = systemInfoMapper.updateById(info);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("管理员修改本站信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员修改本站信息接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, info, result);
        }
    }

    @Override
    public void getSystemStatisticsInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            MainCountVO vo = new MainCountVO();
            // 获取今日注册用户数量
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.ge(User::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
            vo.setUserCount(userMapper.selectCount(userLambdaQueryWrapper));
            // 获取今日发布文章数量
            LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleLambdaQueryWrapper.ge(Article::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
            vo.setPublishCount(articleMapper.selectCount(articleLambdaQueryWrapper));
            // 获取今日登录数量
            vo.setActiveCount(userLoginLogMapper.selectTodayLoginCount(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)));
            // 获取今日订单数量
            LambdaQueryWrapper<PointTradeOrder> pointTradeOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pointTradeOrderLambdaQueryWrapper
                    .ge(PointTradeOrder::getOrderTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
                    .eq(PointTradeOrder::getOrderStatus, 1);
            vo.setOrderCount(pointTradeOrderMapper.selectCount(pointTradeOrderLambdaQueryWrapper));
            result.setData(vo);
        } catch (Exception e) {
            log.error("管理员获取看板数量统计失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取看板数量统计接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, null, result);
        }
    }

    @Override
    public void getSystemRangeStatisticsInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            GetSystemRangeStatisticsInfoVO vo = new GetSystemRangeStatisticsInfoVO();
            GetSystemRangeStatisticsInfoVO.DataInfo dataInfo = new GetSystemRangeStatisticsInfoVO.DataInfo();
            Integer num = 7;
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.YYYY_MM_DD);
            // 获取最近7日注册用户数量
            List<UserRangeCountBo> userRangeCountBos = userMapper.selectRangeCreateTimeCountInfo(num);
            Map<String, Integer> userRangeMap = CollectionUtils.isEmpty(userRangeCountBos) ?
                    new HashMap<>() :
                    userRangeCountBos.stream().collect(Collectors.toMap(UserRangeCountBo::getDate, UserRangeCountBo::getCount, (o1, o2) -> o1));
            // 获取最近7日发布文章数量
            List<ArticleRangeCountBo> articleRangeCountBos = articleMapper.selectRangeCreateTimeCountInfo(num);
            Map<String, Integer> articleRangeMap = CollectionUtils.isEmpty(articleRangeCountBos) ?
                    new HashMap<>() :
                    articleRangeCountBos.stream().collect(Collectors.toMap(ArticleRangeCountBo::getDate, ArticleRangeCountBo::getCount, (o1, o2) -> o1));
            // 获取最近7日登录数量
            List<UserLoginRangeCountBo> userLoginRangeCountBos = userLoginLogMapper.selectRangeCreateTimeCountInfo(num);
            Map<String, Integer> userLoginRangeMap = CollectionUtils.isEmpty(userLoginRangeCountBos) ?
                    new HashMap<>() :
                    userLoginRangeCountBos.stream().collect(Collectors.toMap(UserLoginRangeCountBo::getDate, UserLoginRangeCountBo::getCount, (o1, o2) -> o1));
            // 获取最近7日订单数量
            List<OrderRangeCountBo> orderLoginRangeCountBos = pointTradeOrderMapper.selectRangeCreateTimeCountInfo(num);
            Map<String, Integer> orderLoginRangeMap = CollectionUtils.isEmpty(orderLoginRangeCountBos) ?
                    new HashMap<>() :
                    orderLoginRangeCountBos.stream().collect(Collectors.toMap(OrderRangeCountBo::getDate, OrderRangeCountBo::getCount, (o1, o2) -> o1));
            List<String> weekList = new ArrayList<>();
            GetSystemRangeStatisticsInfoVO.DataVO newUserInfo = new GetSystemRangeStatisticsInfoVO.DataVO();
            GetSystemRangeStatisticsInfoVO.DataVO publishInfo = new GetSystemRangeStatisticsInfoVO.DataVO();
            GetSystemRangeStatisticsInfoVO.DataVO activeInfo = new GetSystemRangeStatisticsInfoVO.DataVO();
            GetSystemRangeStatisticsInfoVO.DataVO ordersInfo = new GetSystemRangeStatisticsInfoVO.DataVO();
            for (int i = num - 1; i >= 0; i--) {
                LocalDate preDate = now.minusDays(i+1);
                LocalDate date = now.minusDays(i);
                String preDateStr = formatter.format(preDate);
                String dateStr = formatter.format(date);
                weekList.add(DateUtil.getWeekStr(date.getDayOfWeek().getValue()));

                Integer userCount = userRangeMap.get(dateStr);
                Integer preUserCount = userRangeMap.get(preDateStr);
                preUserCount = preUserCount == null ? 0: preUserCount;
                newUserInfo.getActualData().add(null == userCount ? 0 : userCount);
                newUserInfo.getExpectedData().add(preUserCount == 0 ? preUserCount + date.getDayOfWeek().getValue() : preUserCount * 2);

                Integer articleCount = articleRangeMap.get(dateStr);
                Integer preArticleCount = articleRangeMap.get(preDateStr);
                preArticleCount = preArticleCount == null ? 0: preArticleCount;
                publishInfo.getActualData().add(null == articleCount ? 0 : articleCount);
                publishInfo.getExpectedData().add(preArticleCount == 0 ? preArticleCount + date.getDayOfWeek().getValue() : preArticleCount * 2);

                Integer userLoginCount = userLoginRangeMap.get(dateStr);
                Integer preUserLoginCount = userLoginRangeMap.get(preDateStr);
                preUserLoginCount = preUserLoginCount == null ? 0: preUserLoginCount;
                activeInfo.getActualData().add(null == userLoginCount ? 0 : userLoginCount);
                activeInfo.getExpectedData().add(preUserLoginCount == 0 ? preUserLoginCount + date.getDayOfWeek().getValue() : preUserLoginCount * 2);

                Integer orderLoginCount = orderLoginRangeMap.get(dateStr);
                Integer preOrderLoginCount = orderLoginRangeMap.get(preDateStr);
                preOrderLoginCount = preOrderLoginCount == null ? 0: preOrderLoginCount;
                ordersInfo.getActualData().add(null == orderLoginCount ? 0 : orderLoginCount);
                ordersInfo.getExpectedData().add(preOrderLoginCount == 0 ? preOrderLoginCount + date.getDayOfWeek().getValue() : preOrderLoginCount * 2);
            }
            dataInfo.setActiveInfo(activeInfo);
            dataInfo.setOrdersInfo(ordersInfo);
            dataInfo.setPublishInfo(publishInfo);
            dataInfo.setNewUserInfo(newUserInfo);
            vo.setWeekList(weekList);
            vo.setLineData(dataInfo);
            result.setData(vo);
        } catch (Exception e) {
            log.error("管理员获取看板数量范围统计失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取看板数量范围统计接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, null, result);
        }
    }


    public static void main(String[] args) {
        LocalDate now = LocalDate.now().plusDays(4);
        System.out.println(now.getDayOfWeek().getValue());
    }
}




