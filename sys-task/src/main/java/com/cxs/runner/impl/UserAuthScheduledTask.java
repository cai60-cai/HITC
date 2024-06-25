package com.cxs.runner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cxs.base.BaseResult;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.mapper.UserAuthScheduledMapper;
import com.cxs.model.UserAuth;
import com.cxs.model.UserAuthScheduled;
import com.cxs.runner.AbstractScheduledTaskJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
  
@Slf4j
@Service
public class UserAuthScheduledTask extends AbstractScheduledTaskJob implements BeanNameAware {

    @Autowired
    private UserAuthScheduledMapper userAuthScheduledMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public void execute(BaseResult result) {
        LambdaQueryWrapper<UserAuthScheduled> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(UserAuthScheduled::getEndTime, LocalDateTime.now());
        List<UserAuthScheduled> userAuthScheduleds = userAuthScheduledMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(userAuthScheduleds)) {
            List<Integer> ids = new ArrayList<>();
            for (UserAuthScheduled userAuthScheduled : userAuthScheduleds) {
                UserAuth userAuth = new UserAuth();
                BeanUtils.copyProperties(userAuthScheduled, userAuth);
                userAuthMapper.updateById(userAuth);
                ids.add(userAuthScheduled.getId());
            }
            userAuthScheduledMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
