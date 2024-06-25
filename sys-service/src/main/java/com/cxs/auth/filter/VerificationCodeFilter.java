package com.cxs.auth.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cxs.base.BaseResult;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.constant.CommonContent;
import com.cxs.constant.ResponseStateConstant;
import com.cxs.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


@Component
public class VerificationCodeFilter extends OncePerRequestFilter {

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!commonConfig.getDev()) {
            // 非登录请求不校验验证码
            if (!CommonContent.LOGIN_REQUEST.equals(httpServletRequest.getRequestURI())) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                String body = getBody(httpServletRequest);
                if (verificationCode(body, httpServletRequest, httpServletResponse)) {
                    filterChain.doFilter(new Request(httpServletRequest, body), httpServletResponse);
                } else {
                    return;
                }

            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private String getBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    class Request extends HttpServletRequestWrapper{
        // 存放JSON数据主体
        private String body;

        public Request(HttpServletRequest request, String context) {
            super(request);
            body = context;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes("UTF-8"));
            ServletInputStream servletInputStream = new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }
            };
            return servletInputStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }


    }

    /**
     * 校验验证码
     * @param httpServletRequest
     */
    public Boolean verificationCode (String body, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        try {
            if (!StringUtils.hasLength(body)) {
                response(response, BaseResult.error().setCode(ResponseStateConstant.PARAMETER_ERROR).setMsg("入参错误!"));
                return false;
            } else {
                JSONObject jsonObject = JSON.parseObject(body);
                if (jsonObject.containsKey("code") && jsonObject.containsKey("codeKey")) {
                    String codeKey = jsonObject.getString("codeKey");
                    String code = jsonObject.getString("code");
                    if (StringUtils.hasLength(codeKey) && StringUtils.hasLength(code)) {
                        String savedCode = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.VALIDATE_CODE_PREFIX, codeKey));
                        if (StringUtils.hasLength(savedCode)) {
                            // 随手清除验证码，不管是失败还是成功，所以客户端应在登录失败时刷新验证码
                            redisUtil.removeKey(redisUtil.getCacheKey(CachePrefixContent.VALIDATE_CODE_PREFIX, codeKey));
                        }
                        // 校验
                        if(!StringUtils.hasLength(savedCode) || !code.equalsIgnoreCase(savedCode)) {
                            response(response, BaseResult.error().setCode(ResponseStateConstant.OPERA_FAIL).setMsg("验证码校验失败"));
                            return false;
                        } else{
                            return true;
                        }

                    } else {
                        response(response, BaseResult.error().setCode(ResponseStateConstant.OPERA_FAIL).setMsg("验证码不能为空!"));
                        return false;
                    }
                } else {
                    response(response, BaseResult.error().setCode(ResponseStateConstant.OPERA_FAIL).setMsg("验证码不能为空!"));
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            response(response, BaseResult.error().setCode(ResponseStateConstant.OPERA_FAIL).setMsg("参数格式错误,请重新输入!"));
            return false;
        }
    }

    private void response(HttpServletResponse response, BaseResult result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(result));
        writer.close();
    }
}
