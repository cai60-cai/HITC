package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.UserAuthScheduled;
import com.cxs.service.UserAuthScheduledService;
import com.cxs.mapper.UserAuthScheduledMapper;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【t_user_auth_scheduled(用户权限禁止定时表)】的数据库操作Service实现
* @createDate 2024-05-03 12:13:25
*/
@Service
public class UserAuthScheduledServiceImpl extends ServiceImpl<UserAuthScheduledMapper, UserAuthScheduled>
    implements UserAuthScheduledService{

}




