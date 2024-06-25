package com.cxs.auth.handler;

import com.alibaba.fastjson.JSON;
import com.cxs.base.BaseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AccessForbiddenHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        BaseResult r = new BaseResult();
        r.setCode(403).setMsg("权限不足，无法访问," + e.getMessage());
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(r));
        writer.close();
    }
}
