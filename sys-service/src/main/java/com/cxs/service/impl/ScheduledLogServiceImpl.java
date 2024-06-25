package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.ScheduledLog;
import com.cxs.service.ScheduledLogService;
import com.cxs.mapper.ScheduledLogMapper;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【t_scheduled_log(定时任务日志记录表)】的数据库操作Service实现
* @createDate 2024-05-04 13:00:01
*/
@Service
public class ScheduledLogServiceImpl extends ServiceImpl<ScheduledLogMapper, ScheduledLog>
    implements ScheduledLogService{

}




