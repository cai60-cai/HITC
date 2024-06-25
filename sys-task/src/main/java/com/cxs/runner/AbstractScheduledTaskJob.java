package com.cxs.runner;

import com.cxs.base.BaseResult;
import com.cxs.constant.CachePrefixContent;
import com.cxs.mapper.ScheduledLogMapper;
import com.cxs.model.Scheduled;
import com.cxs.model.ScheduledLog;
import com.cxs.config.CompletableFutureService;
import com.cxs.service.ScheduledTaskService;
import com.cxs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;

  
@Slf4j
public abstract class AbstractScheduledTaskJob implements ScheduledTaskJob {

    protected String beanName;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Autowired
    private CompletableFutureService completableFutureService;

    @Autowired
    private ScheduledLogMapper scheduledLogMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Map<String, Scheduled> scheduledMap = scheduledTaskService.getScheduledMap();
        ScheduledLog scheduledLog = new ScheduledLog();
        Scheduled scheduled = scheduledMap.get(beanName);
        Boolean flag = Boolean.TRUE;
        String timeStamp = String.valueOf(System.currentTimeMillis() + 300L);
        try {
            Boolean lock = redisUtil.lock(redisUtil.getCacheKey(CachePrefixContent.LOCK_PREFIX, beanName), timeStamp);
            if (lock) {
                BaseResult result = BaseResult.ok();
                scheduledLog.setTaskId(scheduled.getTaskId());
                scheduledLog.setExecuteTime(LocalDateTime.now());
                // 执行定时任务处理逻辑
                execute(result);
                if (result.resOk()) {
                    scheduledLog.setExecuteStatus(Boolean.TRUE);
                } else {
                    scheduledLog.setExecuteStatus(Boolean.FALSE);
                }
                scheduledLog.setExecuteDesc(result.getMsg());
                redisUtil.unLock(redisUtil.getCacheKey(CachePrefixContent.LOCK_PREFIX, beanName), timeStamp);
            } else {
                flag = Boolean.FALSE;
            }
        } catch (Exception e) {
            log.error("定时任务:{}执行失败,{}", scheduled.getTaskName(), e);
            scheduledLog.setExecuteStatus(Boolean.FALSE);
            scheduledLog.setExecuteDesc(e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【】【{}ms】", "定时任务", scheduled.getTaskName(), endTime - startTime);
            if (flag) {
                completableFutureService.runAsyncTask(() -> {
                    scheduledLogMapper.insert(scheduledLog);
                });
            }
        }
    }

    public abstract void execute(BaseResult result);
}
