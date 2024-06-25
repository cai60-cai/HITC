package com.cxs.aspect;
import com.cxs.base.UserSubject;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CommonContent;
import com.cxs.service.UserService;
import com.cxs.utils.UserRoleStatusHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
@Order(1)
public class GlobalPreAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonConfig commonConfig;


    @Pointcut("execution(* com.cxs.controller..*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        UserSubject token = null;
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest requests = (HttpServletRequest) arg;
                token = userService.getUserByToken(requests);
            }
        }
        if (token != null) {
            List<String> authentications = token.getAuthentications();
            if (authentications.size() > 0 && authentications.contains(CommonContent.SYS_VIEW) && !commonConfig.getInnerSuperAdmin().equals(token.getId())) {
                UserRoleStatusHolder.setCurrrentUserRoleStatus(Boolean.TRUE);
            }
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }
}
