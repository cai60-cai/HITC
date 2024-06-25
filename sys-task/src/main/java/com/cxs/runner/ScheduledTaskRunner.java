package com.cxs.runner;

import com.cxs.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ScheduledTaskRunner implements ApplicationRunner {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 开始!");
        scheduledTaskService.initAllTask();
        log.info(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 结束！");
    }
}
