package com.cxs.interceptor;

import com.cxs.utils.UserRoleStatusHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GloableInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空用户角色状态
        UserRoleStatusHolder.cleanCurrrentUserRoleStatus();
    }
}
