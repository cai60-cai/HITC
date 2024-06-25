package com.cxs.service;

import com.cxs.model.Scheduled;

import java.util.Map;


public interface ScheduledTaskService {
    /**
     * 根据任务key 启动任务
     */
    Boolean start(String taskKey, Scheduled scheduled);

    /**
     * 根据任务key 停止任务
     */
    Boolean stop(String taskKey);

    /**
     * 根据任务key 重启任务
     */
    Boolean restart(String taskKey, Scheduled scheduled);

    /**
     * 初始化  ==> 启动所有正常状态的任务
     */
    void initAllTask();

    /**
     * 启动状态
     * @param key
     * @return
     */
    Boolean isStart(String key);

    /**
     * 获取所有的定时任务
     * @return
     */
    Map<String, Scheduled> getScheduledMap();


    /**
     * 执行任务
     * @param scheduled
     */
    void doTask(Scheduled scheduled);
}
