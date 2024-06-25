package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.constant.TaskConstant;
import com.cxs.dto.admin.task.GetTaskLogListDTO;
import com.cxs.dto.admin.task.SaveOrUpdateTaskDTO;
import com.cxs.dto.admin.task.TaskHandleDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ScheduledLogMapper;
import com.cxs.model.Scheduled;
import com.cxs.model.ScheduledLog;
import com.cxs.service.ScheduledService;
import com.cxs.mapper.ScheduledMapper;
import com.cxs.service.ScheduledTaskService;
import com.cxs.vo.admin.task.ScheduleLogVO;
import com.cxs.vo.admin.task.ScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduledServiceImpl extends ServiceImpl<ScheduledMapper, Scheduled> implements ScheduledService{

    @Autowired
    private ScheduledMapper scheduledMapper;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Autowired
    private ScheduledLogMapper scheduledLogMapper;

    @Override
    public void getTaskList(String keyword, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<Scheduled> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasLength(keyword)) {
                wrapper.like(Scheduled::getTaskKey, keyword)
                        .or().like(Scheduled::getTaskName, keyword)
                        .or().like(Scheduled::getTaskDesc, keyword);
            }
            List<Scheduled> scheduledList = scheduledMapper.selectList(wrapper);
            List<ScheduleVO> list = CollectionUtils.isEmpty(scheduledList) ? new ArrayList<>(0) : scheduledList.stream().map(s -> {
                ScheduleVO vo = new ScheduleVO();
                BeanUtils.copyProperties(s, vo);
                return vo;
            }).collect(Collectors.toList());
            list.forEach(t -> {
                t.setRunStatus(scheduledTaskService.isStart(t.getTaskKey()));
            });
            result.setData(list);
        } catch (Exception e) {
            log.error("获取任务列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取任务列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, keyword, result);
        }
    }

    @Override
    public void saveOrUpdateTask(SaveOrUpdateTaskDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Integer taskId = dto.getTaskId();
            Scheduled scheduled = new Scheduled();
            BeanUtils.copyProperties(dto, scheduled);
            if (ObjectUtils.isEmpty(taskId)) {
                // 新增
                int insert = 0;
                try {
                    scheduled.setCreateTime(LocalDateTime.now());
                    insert = scheduledMapper.insert(scheduled);
                } catch (DuplicateKeyException e) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务key已存在");
                    return;
                }
                if (insert != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增任务失败");
                }
                result.setData(scheduled);
            } else {
                // 修改
                int update = 0;
                try {
                    scheduled.setUpdateTime(LocalDateTime.now());
                    update = scheduledMapper.updateById(scheduled);
                } catch (DuplicateKeyException e) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务key已存在");
                    return;
                }
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改任务失败");
                }
            }
        } catch (Exception e) {
            log.error("新增或修改任务失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增或修改任务接口】【{}ms】 \n入参:{}\n出参:{}", "新增或修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void removeTask(Integer taskId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Scheduled scheduled = scheduledMapper.selectById(taskId);
            if (ObjectUtils.isEmpty(scheduled)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务不存在");
            } else {
                if (scheduledTaskService.isStart(scheduled.getTaskKey())) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务处于运行状态,请先停止在操作");
                } else {
                    int delete = scheduledMapper.deleteById(taskId);
                    if (delete != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            log.error("删除任务失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【删除任务接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, taskId, result);
        }
    }

    @Override
    public void taskOperaHandle(TaskHandleDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Scheduled scheduled = scheduledMapper.selectById(dto.getTaskId());
            if (ObjectUtils.isEmpty(scheduled)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务不存在");
            } else {
                Boolean status = scheduledTaskService.isStart(scheduled.getTaskKey());
                if (TaskConstant.TASK_OPERA_START_STATUS.equals(dto.getOperaType())) {
                    if (scheduled.getTaskStatus().equals(0)) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务处于禁用状态、请先启用在操作");
                    } else {
                        if (status) {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务正处于运行状态、请勿执行启动操作");
                        } else {
                            Boolean start = scheduledTaskService.start(scheduled.getTaskKey(), scheduled);
                            if (!start) {
                                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务启动失败");
                            }
                        }
                    }
                } else if (TaskConstant.TASK_OPERA_STOP_STATUS.equals(dto.getOperaType())) {
                    if (!status) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务正处于停止状态、请勿执行停止操作");
                    } else {
                        Boolean stop = scheduledTaskService.stop(scheduled.getTaskKey());
                        if (!stop) {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务停止失败");
                        }
                    }
                } else if (TaskConstant.TASK_OPERA_RESTART_P_STATUS.equals(dto.getOperaType())) {
                    if (!status) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务正处于停止状态、请先执行运行操作");
                    } else {
                        Boolean restart = scheduledTaskService.restart(scheduled.getTaskKey(), scheduled);
                        if (!restart) {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务重启失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("任务操作失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【任务操作接口】【{}ms】 \n入参:{}\n出参:{}", "操作", endTime - startTime, dto, result);
        }
    }

    @Override
    public void executeOnce(Integer taskId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Scheduled scheduled = scheduledMapper.selectById(taskId);
            if (ObjectUtils.isEmpty(scheduled)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",任务不存在");
            } else {
                scheduledTaskService.doTask(scheduled);
            }
        } catch (Exception e) {
            log.error("执行任务失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【执行任务接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, taskId, result);
        }
    }

    @Override
    public void getTaskLog(GetTaskLogListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            IPage<ScheduledLog> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<ScheduledLog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScheduledLog::getTaskId, dto.getTaskId());
            Boolean executeStatus = dto.getExecuteStatus();
            LocalDateTime s = dto.getStartTime();
            LocalDateTime e = dto.getEndTime();
            if (!ObjectUtils.isEmpty(executeStatus)) {
                queryWrapper.eq(ScheduledLog::getExecuteStatus, executeStatus);
            }
            if (!ObjectUtils.isEmpty(s) && ObjectUtils.isEmpty(e)) {
                queryWrapper.ge(ScheduledLog::getExecuteTime, s);
            } else if (ObjectUtils.isEmpty(s) && !ObjectUtils.isEmpty(e)) {
                queryWrapper.le(ScheduledLog::getExecuteTime, e);
            } else if (!ObjectUtils.isEmpty(s) && !ObjectUtils.isEmpty(e)){
                queryWrapper.ge(ScheduledLog::getExecuteTime, s)
                        .le(ScheduledLog::getExecuteTime, e);
            }
            queryWrapper.orderByDesc(ScheduledLog::getExecuteTime);
            IPage<ScheduledLog> logIPage = scheduledLogMapper.selectPage(page, queryWrapper);
            List<ScheduleLogVO> voList = CollectionUtils.isEmpty(logIPage.getRecords()) ? new ArrayList<>(0) :
                    logIPage.getRecords().stream().map(r -> {
                        ScheduleLogVO vo = new ScheduleLogVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
            BasePageBean pageBean = BasePageBean.builder()
                    .pageNum(logIPage.getCurrent())
                    .pageSize(logIPage.getSize())
                    .total(logIPage.getTotal())
                    .pages(logIPage.getPages())
                    .data(voList)
                    .build();
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取定时任务日志失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取定时任务日志接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, dto, result);
        }
    }
}




