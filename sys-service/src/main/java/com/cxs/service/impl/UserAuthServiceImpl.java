package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.UserAuth;
import com.cxs.service.UserAuthService;
import com.cxs.mapper.UserAuthMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【t_user_auth(用户权限表)】的数据库操作Service实现
* @createDate 2024-05-18 11:09:06
*/
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth>
    implements UserAuthService{

}




