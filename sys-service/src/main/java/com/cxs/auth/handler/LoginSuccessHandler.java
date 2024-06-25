package com.cxs.auth.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cxs.base.UserSubject;
import com.cxs.base.BaseResult;
import com.cxs.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        BaseResult r = new BaseResult();
        Object principal = authentication.getPrincipal();
        User user = (User) principal;
        List<String> auths = CollectionUtils.isEmpty(user.getAuthorities()) ? new ArrayList<>(0) :
                user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
        String token = jwtUtil.generateToken(UserSubject.builder()
                        .id(user.getUsername())
                        .username(user.getUsername())
                        .authentications(auths).build());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", principal);
        r.setCode(200).setData(map).setMsg("登陆成功");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(r));
        writer.close();
    }
}
