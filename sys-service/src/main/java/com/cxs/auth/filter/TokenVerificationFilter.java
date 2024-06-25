package com.cxs.auth.filter;

import com.alibaba.fastjson.JSON;
import com.cxs.base.BaseResult;
import com.cxs.base.Token;
import com.cxs.base.UserSubject;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.constant.CommonContent;
import com.cxs.constant.ResponseStateConstant;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.utils.JwtUtil;
import com.cxs.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class TokenVerificationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (ignore(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader(CommonContent.ACCESS_TOKEN);
        if (!StringUtils.hasLength(header)) {
            header = request.getHeader(CommonContent.WEBSOCKET_PROTOCOL);
            if (!StringUtils.hasLength(header)) {
                Cookie[] cookies = request.getCookies();
                if (ObjectUtils.isEmpty(cookies) || cookies.length == 0) {
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    for (Cookie cookie : cookies) {
                        if (CommonContent.ACCESS_TOKEN.equals(cookie.getName())) {
                            header = cookie.getValue();
                        }
                        if (CommonContent.ADMIN_TOKEN.equals(cookie.getName())) {
                            header = cookie.getValue();
                        }
                    }
                }
            }
        }
        if (!StringUtils.hasLength(header)) {
            filterChain.doFilter(request, response);
            return;
        }
        Token token = null;
        List<String> strings = null;
        try {
            String tokenStr = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, header.trim()));
            if (!StringUtils.hasLength(tokenStr)) {
                response(response, BaseResult.error().setCode(ResponseStateConstant.NO_LOGIN).setMsg("用户认证信息已过期"));
                return;
            }
            if (jwtUtil.validTokenIssued(tokenStr)) {
                response(response, BaseResult.error().setCode(ResponseStateConstant.NO_LOGIN).setMsg("用户认证信息已过期"));
                return;
            }
            // 校验信息是否正确，省略
            token = jwtUtil.parseToken(tokenStr);
            strings = token.getUser().getAuthentications();
            Authentication context = new UsernamePasswordAuthenticationToken(
                    token.getUser().getUsername(),
                    token.getUser().getUsername(),
                    AuthorityUtils.createAuthorityList(strings.toArray(new String[0]))
            );
            SecurityContextHolder.getContext().setAuthentication(context);
            Long expire = redisUtil.getExpire(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, header));
            // 有效期小于15分钟，续时
            if (expire <= 900L) {
                redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, header), jwtUtil.generateToken(token.getUser()), commonConfig.getValidityTime(), TimeUnit.MINUTES);
                Cookie cookie = new Cookie(CommonContent.ACCESS_TOKEN, header);
                cookie.setPath("/");
                cookie.setDomain(commonConfig.getDomian());
                cookie.setMaxAge((commonConfig.getValidityTime().intValue() * 60));
                response.addCookie(cookie);
            }
            // 放行
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                BaseResult error = BaseResult.error();
                error.setMsg(CurrencyErrorEnum.UNAUTHORIZED.getMsg());
                error.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                response(response, error);
            } else if (e instanceof ExpiredJwtException) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                BaseResult error = BaseResult.error();
                error.setMsg(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getMsg());
                error.setCode(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getCode());
                response(response, error);
            } else if (e instanceof JwtException) {
                BaseResult error = BaseResult.error();
                error.setMsg(CurrencyErrorEnum.UNAUTHORIZED.getMsg());
                error.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                response(response, error);
            } else {
                throw e;
            }
        }
    }

    private boolean ignore(String requestURI) {
        List<String> list = Arrays.asList(commonConfig.getIgnoreUrl());
        if (CollectionUtils.isEmpty(list)) {
            return false;
        } else {
            for (String path : list) {
                if (path.contains("**") || path.contains("*")) {
                    if (requestURI.startsWith(path.replace("**", ""))) {
                        return true;
                    }
                } else {
                    if (requestURI.equals(path)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private void response(HttpServletResponse response, BaseResult result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(result));
        writer.close();
    }
}
