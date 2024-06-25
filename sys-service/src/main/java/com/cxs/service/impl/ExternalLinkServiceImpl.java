package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.dto.admin.external.GetExternalLinkListDTO;
import com.cxs.dto.external.ApplyExternalLinkDTO;
import com.cxs.dto.external.ApproveExternalFriendLinkDTO;
import com.cxs.dto.external.SaveOrUpdateExternalFriendLinkDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.model.ExternalLink;
import com.cxs.model.NavLink;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.service.ExternalLinkService;
import com.cxs.mapper.ExternalLinkMapper;
import com.cxs.service.UserService;
import com.cxs.utils.RedisUtil;
import com.cxs.vo.main.ExternalLinkVO;
import com.cxs.vo.technology.TechnologyTypeVO;
import com.cxs.vo.user.UserVO;
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

@Service
@Slf4j
public class ExternalLinkServiceImpl extends ServiceImpl<ExternalLinkMapper, ExternalLink> implements ExternalLinkService{

    @Autowired
    private ExternalLinkMapper externalLinkMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public void getExternalLinkList(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<ExternalLinkVO> voList = new ArrayList<>();
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_EXTERNALLINK_LIST);
            if (!ObjectUtils.isEmpty(value)) {
                voList = JSON.parseArray(JSON.toJSONString(value), ExternalLinkVO.class);
            } else {
                LambdaQueryWrapper<ExternalLink> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ExternalLink::getExternalLinkStatus, 1);
                queryWrapper.orderByDesc(ExternalLink::getCreateTime);
                List<ExternalLink> externalLinks = externalLinkMapper.selectList(queryWrapper);
                voList = CollectionUtils.isEmpty(externalLinks) ? new ArrayList<>(0) :
                        externalLinks.stream().map(e -> {
                            ExternalLinkVO vo = new ExternalLinkVO();
                            BeanUtils.copyProperties(e, vo);
                            return vo;
                        }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(voList)) {
                    redisUtil.set(CachePrefixContent.INDEX_EXTERNALLINK_LIST, voList, commonConfig.getCacheShortTime(), TimeUnit.MINUTES);
                }
            }
            result.setData(voList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户获取外部链接失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取外部链接接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void applyExternalLink(ApplyExternalLinkDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (!userAuth.getApplyExternalAuth()) {
                result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法申请友链,请联系管理员");
            } else {
                ExternalLink link = new ExternalLink();
                BeanUtils.copyProperties(dto, link);
                link.setExternalLinkUserId(userByToken.getId());
                link.setCreateTime(LocalDateTime.now());
                int insert = 0;
                try {
                    insert = externalLinkMapper.insert(link);
                } catch (DuplicateKeyException e) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接名已存在");
                    return;
                }
                if (insert != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户获取外部链接失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取外部链接接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getExternalFriendLinkList(GetExternalLinkListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            IPage<ExternalLink> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<ExternalLink> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ExternalLink::getExternalLinkStatus, dto.getType());
            queryWrapper.orderByDesc(ExternalLink::getCreateTime);
            IPage<ExternalLink> iPage = externalLinkMapper.selectPage(page, queryWrapper);
            BasePageBean pageBean = BasePageBean.builder()
                    .pageNum(iPage.getCurrent())
                    .pageSize(iPage.getSize())
                    .pages(iPage.getPages())
                    .total(iPage.getTotal())
                    .data(
                            CollectionUtils.isEmpty(iPage.getRecords()) ? new ArrayList<>(0) :
                                    iPage.getRecords().stream().map(e -> {
                                        com.cxs.vo.admin.external.ExternalLinkVO vo = new com.cxs.vo.admin.external.ExternalLinkVO();
                                        BeanUtils.copyProperties(e, vo);
                                        User byId = userService.getById(e.getExternalLinkUserId());
                                        UserVO userVO = new UserVO();
                                        BeanUtils.copyProperties(byId, userVO);
                                        vo.setUser(userVO);
                                        return vo;
                                    }).collect(Collectors.toList()))
                    .build();
            result.setData(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员获取外部链接列表失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取外部链接列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void approveExternalFriendLink(ApproveExternalFriendLinkDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            ExternalLink link = externalLinkMapper.selectById(dto.getExternalLinkId());
            if (ObjectUtils.isEmpty(link)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接不存在");
            } else {
                ExternalLink updateLink = new ExternalLink();
                BeanUtils.copyProperties(dto, updateLink);
                updateLink.setUpdateTime(LocalDateTime.now());
                int update = externalLinkMapper.updateById(updateLink);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员审核外部链接失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员审核外部链接接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void saveExternalFriendLink(SaveOrUpdateExternalFriendLinkDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject byToken = userService.getUserByToken(request);
            ExternalLink link = new ExternalLink();
            BeanUtils.copyProperties(dto, link);
            link.setCreateTime(LocalDateTime.now());
            link.setExternalLinkUserId(byToken.getId());
            int insert = 0;
            try {
                insert = externalLinkMapper.insert(link);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增链接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员新增外部链接失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员新增外部链接接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateExternalFriendLink(SaveOrUpdateExternalFriendLinkDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            ExternalLink linkInfo = externalLinkMapper.selectById(dto.getExternalLinkId());
            if (ObjectUtils.isEmpty(linkInfo)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接不存在");
            } else {
                ExternalLink link = new ExternalLink();
                BeanUtils.copyProperties(dto, link);
                link.setUpdateTime(LocalDateTime.now());
                int update = 0;
                try {
                    update = externalLinkMapper.updateById(link);
                } catch (DuplicateKeyException e) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",链接名称已存在");
                    return;
                }
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改链接失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员修改外部链接失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员修改外部链接接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void deleteExternalFriendLink(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            ExternalLink linkInfo = externalLinkMapper.selectById(id);
            if (ObjectUtils.isEmpty(linkInfo)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该链接不存在");
            } else {
                int delete = externalLinkMapper.deleteById(id);
                if (delete != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员删除外部链接失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员删除外部链接接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }
}




