package com.cxs.runner.impl;

import com.cxs.base.BaseResult;
import com.cxs.runner.AbstractScheduledTaskJob;
import com.cxs.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
public class TaskTest extends AbstractScheduledTaskJob implements BeanNameAware {

    @Override
    public void execute(BaseResult result) {
        System.out.println("定时任务执行测试:" + DateUtil.localDateTimeToString(LocalDateTime.now(), DateUtil.YYYY_MM_DD_HH_MM_SS));
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
