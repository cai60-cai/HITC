package com.cxs.auth.handler;

import com.alibaba.fastjson.JSON;
import com.cxs.base.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class AuthenticationHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        BaseResult r = new BaseResult();
        r.setCode(401).setMsg("toke认证失败,请重新认证");
        // 记录详细的日志


        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(r));
        writer.close();
    }
}
