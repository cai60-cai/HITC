package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.UserReward;
import com.cxs.service.UserRewardService;
import com.cxs.mapper.UserRewardMapper;
import org.springframework.stereotype.Service;


@Service
public class UserRewardServiceImpl extends ServiceImpl<UserRewardMapper, UserReward>
    implements UserRewardService{

}




