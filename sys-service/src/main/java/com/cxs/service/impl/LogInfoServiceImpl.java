package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.bo.LogInfoBo;
import com.cxs.dto.admin.log.GetLogListDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.LogInfo;
import com.cxs.model.Scheduled;
import com.cxs.service.LogInfoService;
import com.cxs.mapper.LogInfoMapper;
import com.cxs.vo.admin.log.LogInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@Slf4j
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService{

    @Autowired
    private LogInfoMapper logInfoMapper;

    @Override
    public void getLogList(GetLogListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            IPage<LogInfoBo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            Page<LogInfoBo> logInfoPage =  logInfoMapper.selectListByQuery(page, dto);
            BasePageBean pageBean = BasePageBean.builder()
                    .pageNum(page.getCurrent())
                    .pageSize(page.getSize())
                    .total(page.getTotal())
                    .pages(page.getPages())
                    .data(CollectionUtils.isEmpty(logInfoPage.getRecords()) ? new ArrayList<>(0) :
                                    JSON.parseArray(JSON.toJSONString(page.getRecords()), LogInfoVO.class))
                    .build();
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("查询日志失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【查询日志接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }
}




