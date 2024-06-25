package com.cxs.runner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cxs.base.BaseResult;
import com.cxs.mapper.ScheduledLogMapper;
import com.cxs.model.ScheduledLog;
import com.cxs.runner.AbstractScheduledTaskJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

  
@Slf4j
@Service
public class ClearInvalidDataTask extends AbstractScheduledTaskJob implements BeanNameAware {

    @Autowired
    private ScheduledLogMapper scheduledLogMapper;

    @Override
    public void execute(BaseResult result) {
        LambdaQueryWrapper<ScheduledLog> queryWrapper = new LambdaQueryWrapper<>();
        LocalDateTime date = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(7);
        queryWrapper.le(ScheduledLog::getExecuteTime, date);
        scheduledLogMapper.delete(queryWrapper);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
