package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.UserRole;
import com.cxs.service.UserRoleService;
import com.cxs.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




