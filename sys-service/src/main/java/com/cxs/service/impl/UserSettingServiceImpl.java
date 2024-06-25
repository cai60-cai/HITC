package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.UserSetting;
import com.cxs.service.UserSettingService;
import com.cxs.mapper.UserSettingMapper;
import org.springframework.stereotype.Service;

@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting>
    implements UserSettingService{

}




