package com.cxs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
@EnableAutoConfiguration
public class ThreadPoolConfig {
    @Value("${dream.executorService.dreamTask.coreSize:150}")
    private Integer coreSize;

    @Value("${dream.executorService.dreamTask.maxSize:250}")
    private Integer maxSize;

    @Value("${dream.executorService.dreamTask.queueCap:20}")
    private Integer queueCap;

    @Value("${dream.executorService.dreamTask.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;

    @Value("${dream.executorService.dreamTask.threadNamePrefix:Async-Service-}")
    private String threadNamePrefix;

    @Bean("dreamTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCap);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
//        todo
        // executor.setTaskDecorator(new ThreadLocalTaskDecorator());

        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                log.error("dreamTaskExecutor no available thread, task: {}, ThreadPoolExecutor: {}", r, e);
                super.rejectedExecution(r, e);
            }
        });
        // 初始化
        executor.initialize();
        return executor;
    }
}
