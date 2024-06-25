package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.message.ReadMessageDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.PointRechargeType;
import com.cxs.model.PointTradeOrder;
import com.cxs.model.SystemMessage;
import com.cxs.service.SystemMessageService;
import com.cxs.mapper.SystemMessageMapper;
import com.cxs.service.UserService;
import com.cxs.vo.message.SysMessageVO;
import com.cxs.vo.user.UserOrderListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author DELL
* @description 针对表【t_system_message(系统通知表)】的数据库操作Service实现
* @createDate 2024-05-10 15:54:24
*/
@Slf4j
@Service
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessage>
    implements SystemMessageService{

    @Autowired
    private SystemMessageMapper systemMessageMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketSysMessage webSocketSysMessage;

    @Override
    public void getSystemMessage(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<SystemMessage> systemMessageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            systemMessageLambdaQueryWrapper
                    .eq(SystemMessage::getReceiveUserId, userByToken.getId())
                    .orderByDesc(SystemMessage::getCreateTime);
            IPage<SystemMessage> iPage = systemMessageMapper.selectPage(page, systemMessageLambdaQueryWrapper);
            List<SystemMessage> systemMessages = iPage.getRecords();
            List<SysMessageVO> voList = CollectionUtils.isEmpty(systemMessages) ? new ArrayList<>(0) : systemMessages.stream().map(f -> {
                SysMessageVO vo = new SysMessageVO();
                BeanUtils.copyProperties(f, vo);
                return vo;
            }).collect(Collectors.toList());

            LambdaQueryWrapper<SystemMessage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(SystemMessage::getReceiveUserId, userByToken.getId())
                    .eq(SystemMessage::getIsRead, 0);
            JSONObject other = new JSONObject();
            other.put("unReadNum", systemMessageMapper.selectCount(queryWrapper));
            pageBean.setOther(other);
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取消息通知列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取消息通知列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void readSysMessage(ReadMessageDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Set<Integer> messageIdList = dto.getMessageIdList();
            if (!CollectionUtils.isEmpty(messageIdList)) {
                UserSubject userByToken = userService.getUserByToken(request);
                systemMessageMapper.updateReadFlagByTargetIdAndIds(userByToken.getId(), messageIdList);
            }
        } catch (Exception e) {
            log.error("设置消息已读失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【设置消息已读接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void saveSysMessage(SystemMessage data, Boolean send, String userId, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            int insert = systemMessageMapper.insert(data);
            if (insert == 1) {
                if (send) {
                    SysMessageVO vo = new SysMessageVO();
                    BeanUtils.copyProperties(data, vo);
                    webSocketSysMessage.sendMessage(userId, vo);
                }
            }
        } catch (Exception e) {
            log.error("保存消息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【保存消息接口】【{}ms】 \n入参:{}\n出参:{}", "保存", endTime - startTime, JSON.toJSONString(data), result);
        }
    }
}




