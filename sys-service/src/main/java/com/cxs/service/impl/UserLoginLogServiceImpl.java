package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.UserLoginLog;
import com.cxs.service.UserLoginLogService;
import com.cxs.mapper.UserLoginLogMapper;
import org.springframework.stereotype.Service;


@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService{

}




