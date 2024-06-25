package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.constant.CommonContent;
import com.cxs.dto.admin.nav.CreateOrUpdateNavLinkDTO;
import com.cxs.dto.admin.nav.NavLinkOrderMoveOrToppingOrDelDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.NavLink;
import com.cxs.service.NavLinkService;
import com.cxs.mapper.NavLinkMapper;
import com.cxs.utils.RedisUtil;
import com.cxs.vo.main.NavLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class NavLinkServiceImpl extends ServiceImpl<NavLinkMapper, NavLink> implements NavLinkService {

    @Autowired
    private NavLinkMapper navLinkMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public void getLinkList(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_NAV_LIST);
            List<NavLinkVO> voList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(value)) {
                voList = JSON.parseArray(JSON.toJSONString(value), NavLinkVO.class);
            } else {
                QueryWrapper<NavLink> wrapper = new QueryWrapper<>();
                wrapper.orderByAsc("nav_order");
                List<NavLink> linkList = navLinkMapper.selectList(wrapper);
                voList = CollectionUtils.isEmpty(linkList) ? new ArrayList<>(0) :
                        linkList.stream().map(l -> {
                            NavLinkVO vo = new NavLinkVO();
                            BeanUtils.copyProperties(l, vo);
                            return vo;
                        }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(voList)) {
                    redisUtil.set(CachePrefixContent.INDEX_NAV_LIST, voList, commonConfig.getCacheShortTime(), TimeUnit.MINUTES);
                }
            }
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取NavLink列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取NavLink列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void createNavLink(CreateOrUpdateNavLinkDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            NavLink navLink = new NavLink();
            BeanUtils.copyProperties(dto, navLink);
            NavLink maxNavLink = navLinkMapper.selectMaxOrderNavLink();
            if (ObjectUtils.isEmpty(maxNavLink)) {
                navLink.setNavOrder(1);
            } else {
                navLink.setNavOrder(maxNavLink.getNavOrder() + 1);
            }
            int insert = 0;
            try {
                insert = navLinkMapper.insert(navLink);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接名称已存在");
                return;
            }
            if (insert == 1) {
                result.setData(navLink);
                result.setMsg("链接添加成功");
            } else {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加NavLink失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增/修改NavLink接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateNavLink(CreateOrUpdateNavLinkDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            NavLink navLink = new NavLink();
            BeanUtils.copyProperties(dto, navLink);
            int insert = 0;
            try {
                insert = navLinkMapper.updateById(navLink);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接名称已存在");
                return;
            }
            if (insert == 1) {
                result.setMsg("链接修改成功");
            } else {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改NavLink失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增/修改NavLink接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void navLinkOrderMoveOrToppingOrDel(NavLinkOrderMoveOrToppingOrDelDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            NavLink navLink = navLinkMapper.selectById(dto.getNavId());
            if (ObjectUtils.isEmpty(navLink)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接不存在");
            } else {
                Integer operaType = dto.getOperaType();
                if (CommonContent.ORDER_TOPPING_CODE.equals(operaType)) {
                    // 置顶
                    if (navLink.getNavOrder().equals(1)) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接已在首位，请勿执行置顶操作");
                    } else {
                        List<NavLink> navLinkList = navLinkMapper.selectNavLinkByOrderRange(1, navLink.getNavOrder());
                        List<NavLink> surplusNavLinkList = navLinkList.stream()
                                .filter(p -> !p.getNavId().equals(navLink.getNavId()))
                                .collect(Collectors.toList());
                        List<NavLink> targetNavLinkList = new ArrayList<>();
                        targetNavLinkList.add(NavLink.builder()
                                .navId(navLink.getNavId()).navOrder(1).build());
                        surplusNavLinkList.stream().forEach(p -> targetNavLinkList.add(
                                    NavLink.builder().navId(p.getNavId()).navOrder(p.getNavOrder() + 1).build()));
                        Integer batchUpdate = navLinkMapper.updateByPrimaryKeyBatchs(targetNavLinkList);
                        if (batchUpdate != targetNavLinkList.size()) {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",链接置顶失败");
                        } else {
                            result.setMsg("链接置顶成功");
                        }
                    }
                } else if (CommonContent.ORDER_UP_CODE.equals(operaType)) {
                    // 上移
                    if (navLink.getNavOrder().equals(1)) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接已在首位，请勿执行上移操作");
                    } else {
                        // 根据顺序获取商品
                        NavLink preNavLinkInfo = navLinkMapper.selectNavLinkByOrder(navLink.getNavOrder() - 1);
                        if (ObjectUtils.isEmpty(preNavLinkInfo)) {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接顺序异常");
                        } else {
                            NavLink originNavLink = NavLink.builder()
                                    .navId(navLink.getNavId())
                                    .navOrder(preNavLinkInfo.getNavOrder())
                                    .build();
                            NavLink preNavLink = NavLink.builder()
                                    .navId(preNavLinkInfo.getNavId())
                                    .navOrder(navLink.getNavOrder())
                                    .build();
                            int navLinkUpdate = navLinkMapper.updateById(originNavLink);
                            int preNavLinkUpdate = navLinkMapper.updateById(preNavLink);
                            if (navLinkUpdate != 1 || preNavLinkUpdate != 1) {
                                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",链接上移失败");
                            } else {
                                result.setMsg("链接上移成功");
                            }
                        }
                    }
                } else if (CommonContent.ORDER_DOWN_CODE.equals(operaType)) {
                    // 下移
                    NavLink orderMaxNavLinkInfo = navLinkMapper.selectMaxOrderNavLink();
                    if (navLink.getNavOrder().equals(orderMaxNavLinkInfo.getNavOrder())) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接正处于末尾,请勿执行下移操作!");
                    } else {
                        // 根据顺序获取商品
                        NavLink postNavLinkInfo = navLinkMapper.selectNavLinkByOrder(navLink.getNavOrder() + 1);
                        if (ObjectUtils.isEmpty(postNavLinkInfo)) {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接顺序异常");
                        } else {
                            NavLink originNavLink = NavLink.builder()
                                    .navId(navLink.getNavId())
                                    .navOrder(postNavLinkInfo.getNavOrder())
                                    .build();
                            NavLink postNavLink = NavLink.builder()
                                    .navId(postNavLinkInfo.getNavId())
                                    .navOrder(navLink.getNavOrder())
                                    .build();
                            int navLinkUpdate = navLinkMapper.updateById(originNavLink);
                            int postNavLinkUpdate = navLinkMapper.updateById(postNavLink);
                            if (navLinkUpdate != 1 || postNavLinkUpdate != 1) {
                                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",链接下移失败");
                            } else {
                                result.setMsg("链接下移成功");
                            }
                        }
                    }
                } else if (CommonContent.ORDER_DEL_CODE.equals(operaType)) {
                    // 删除
                    Integer del = navLinkMapper.deleteById(navLink.getNavId());
                    if (del != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",链接删除失败");
                    } else {
                        List<NavLink> navLinkInfoList = navLinkMapper.selectNavLinkAfterOrder(navLink.getNavOrder());
                        if (!CollectionUtils.isEmpty(navLinkInfoList)) {
                            Integer update = navLinkMapper.updateByPrimaryKeyBatchs(navLinkInfoList.stream().map(a -> NavLink.builder()
                                    .navId(a.getNavId())
                                    .navOrder(a.getNavOrder() - 1)
                                    .build()
                            ).collect(Collectors.toList()));
                            if (update != navLinkInfoList.size()) {
                                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",链接删除失败");
                            }
                        }
                        result.setMsg("链接删除成功");
                    }
                } else {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",操作类型不存在");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("NavLink操作失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【NavLink顺序上移、下移、置顶、删除接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getRealtimeLinkList(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<NavLinkVO> voList = new ArrayList<>();
            QueryWrapper<NavLink> wrapper = new QueryWrapper<>();
            wrapper.orderByAsc("nav_order");
            List<NavLink> linkList = navLinkMapper.selectList(wrapper);
            voList = CollectionUtils.isEmpty(linkList) ? new ArrayList<>(0) :
                    linkList.stream().map(l -> {
                        NavLinkVO vo = new NavLinkVO();
                        BeanUtils.copyProperties(l, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取NavLink列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取NavLink列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }
}




