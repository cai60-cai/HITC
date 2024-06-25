package com.cxs.auth.handler;

import com.alibaba.fastjson.JSON;
import com.cxs.base.BaseResult;
import com.cxs.constant.ResponseStateConstant;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        BaseResult r = new BaseResult();
        r.setCode(ResponseStateConstant.OPERA_FAIL).setMsg(e.getMessage());
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(r));
        writer.close();
    }
}
