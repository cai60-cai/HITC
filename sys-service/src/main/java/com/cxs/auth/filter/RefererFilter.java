package com.cxs.auth.filter;

import com.cxs.config.CommonConfig;
import com.cxs.constant.CommonContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class RefererFilter extends OncePerRequestFilter {

    @Autowired
    private CommonConfig commonConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(CommonContent.FILE_ACCESS_PREFIX)) {
            Boolean dev = commonConfig.getDev();
            String referer = request.getHeader("Referer");
            if (StringUtils.hasLength(referer)) {
                if (dev) {
                    if (referer.contains("127.0.0.1") || referer.contains(commonConfig.getDomian())) {
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendRedirect(commonConfig.getDefaultImageUrl());
                    }
                } else {
                    if (referer.contains(commonConfig.getDomian())) {
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendRedirect(commonConfig.getDefaultImageUrl());
                    }
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
