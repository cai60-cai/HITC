package com.cxs.aspect;

import com.alibaba.fastjson.JSON;
import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.UserSubject;
import com.cxs.constant.CommonContent;
import com.cxs.model.LogInfo;
import com.cxs.config.CompletableFutureService;
import com.cxs.service.LogInfoService;
import com.cxs.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Aspect
@Component
@Order(2)
public class MarkLogAspect {

    @Autowired
    private CompletableFutureService completableFutureService;

    @Autowired
    private LogInfoService logInfoService;

    @Autowired
    private UserService userService;

    @Pointcut("@annotation(com.cxs.aspect.annotation.MarkLog)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        LogInfo info = new LogInfo();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        info.setOperaMethod(method.getName());
        // 获得当前登录用户
        if (args.length > 1 && args[1] instanceof HttpServletRequest) {
            HttpServletRequest requests = (HttpServletRequest) args[1];
            UserSubject token = userService.getUserByToken(requests);
            info.setOperaUserId(token.getId());
        }
        MarkLog annotation = method.getAnnotation(MarkLog.class);
        if (args != null && !(args[0] instanceof HttpServletRequest) && !(args[0] instanceof HttpServletResponse)) {
            info.setParam(JSON.toJSONString(args[0]));
        }
        info.setOperaDesc(annotation.desc());
        Object result = null;
        try {
            result = joinPoint.proceed();
            if (result != null) {
                info.setResponse(JSON.toJSONString(result));
            }
            info.setOperaResult(CommonContent.OPERA_SUCCESS);
        } catch (Throwable e) {
            e.printStackTrace();
            info.setResponse(JSON.toJSONString(result));
            info.setOperaResult(CommonContent.OPERA_FAILARE);
            info.setOperaErrorWhy(e.getMessage());
            throw e;
        } finally {
            info.setOperaTime(LocalDateTime.now());
            // 保存日志
            completableFutureService.runAsyncTask(() -> {
                logInfoService.save(info);
            });
        }
        return result;
    }
}
