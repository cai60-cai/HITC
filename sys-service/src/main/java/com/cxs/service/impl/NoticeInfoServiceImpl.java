package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.bo.NoticeInfoBo;
import com.cxs.dto.admin.notice.GetNoticeListDTO;
import com.cxs.dto.admin.notice.SaveOrUpdateNoticeDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.exception.CurrencyException;
import com.cxs.model.NoticeInfo;
import com.cxs.model.User;
import com.cxs.service.NoticeInfoService;
import com.cxs.mapper.NoticeInfoMapper;
import com.cxs.service.UserService;
import com.cxs.vo.admin.notice.NoticeInfoVO;
import com.cxs.vo.admin.notice.NoticeListVO;
import com.cxs.vo.main.MainSimpleNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NoticeInfoServiceImpl extends ServiceImpl<NoticeInfoMapper, NoticeInfo> implements NoticeInfoService{

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    @Autowired
    private UserService userService;

    @Override
    public void getNoticeList(GetNoticeListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            IPage<NoticeInfoBo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            Page<NoticeInfoBo> logInfoPage =  noticeInfoMapper.selectListByQuery(page, dto);
            List<NoticeListVO> voList = CollectionUtils.isEmpty(logInfoPage.getRecords()) ? new ArrayList<>(0) :
                    JSON.parseArray(JSON.toJSONString(page.getRecords()), NoticeListVO.class);
            BasePageBean pageBean = BasePageBean.builder()
                    .pageNum(page.getCurrent())
                    .pageSize(page.getSize())
                    .total(page.getTotal())
                    .pages(page.getPages())
                    .data(voList)
                    .build();
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("查询公告列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【查询公告列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void saveOrUpdateNotice(SaveOrUpdateNoticeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Integer id = dto.getId();
            NoticeInfo noticeInfo = new NoticeInfo();
            BeanUtils.copyProperties(dto, noticeInfo);
            noticeInfo.setPublishUserId(userByToken.getId());
            if (ObjectUtils.isEmpty(id)) {
                noticeInfo.setPublishTime(LocalDateTime.now());
                int insert = noticeInfoMapper.insert(noticeInfo);
                if (insert != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            } else {
                noticeInfo.setUpdateTime(LocalDateTime.now());
                int update = noticeInfoMapper.updateById(noticeInfo);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("新增或修改公告失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增或修改接口】【{}ms】 \n入参:{}\n出参:{}", "新增或修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void deleteNotice(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            NoticeInfo noticeInfo = noticeInfoMapper.selectById(id);
            if (ObjectUtils.isEmpty(noticeInfo)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",公告不存在");
            } else {
                int delete = noticeInfoMapper.deleteById(id);
                if (delete != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("删除公告失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【删除公告接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }

    @Override
    public void getNoticeInfo(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            NoticeInfo noticeInfo = noticeInfoMapper.selectById(id);
            if (ObjectUtils.isEmpty(noticeInfo)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",公告不存在");
            } else {
                NoticeInfoVO vo = new NoticeInfoVO();
                BeanUtils.copyProperties(noticeInfo, vo);
                NoticeInfoVO.UserVO userVO = new NoticeInfoVO.UserVO();
                User byId = userService.getById(noticeInfo.getPublishUserId());
                BeanUtils.copyProperties(byId, userVO);
                vo.setUser(userVO);
                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("管理员查询公告详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员查询公告详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }

    @Override
    public void getPushNoticeList(Integer type, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<NoticeInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(NoticeInfo::getId, NoticeInfo::getNoticeTitle);
            if (type == 1) {
                wrapper.eq(NoticeInfo::getIsPush, 1).orderByDesc(NoticeInfo::getUpdateTime, NoticeInfo::getPublishTime);
            } else {
                wrapper.orderByDesc(NoticeInfo::getPublishTime, NoticeInfo::getPublishTime).last("limit 10");
            }
            List<NoticeInfo> noticeInfos = noticeInfoMapper.selectList(wrapper);
            List<MainSimpleNoticeVO> voList = CollectionUtils.isEmpty(noticeInfos) ? new ArrayList<>(0) :
                    noticeInfos.stream().map(n -> {
                        MainSimpleNoticeVO vo = new MainSimpleNoticeVO();
                        BeanUtils.copyProperties(n, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("用户获取首页轮询公告失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取首页轮询公告接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }
}




