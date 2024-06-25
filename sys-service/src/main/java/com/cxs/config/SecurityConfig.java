package com.cxs.config;

import com.cxs.auth.filter.RefererFilter;
import com.cxs.auth.filter.TokenVerificationFilter;
import com.cxs.auth.filter.VerificationCodeFilter;
import com.cxs.auth.handler.AccessForbiddenHandler;
import com.cxs.auth.handler.AuthenticationHandler;
import com.cxs.auth.handler.LoginFailHandler;
import com.cxs.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*
    创建加密编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private TokenVerificationFilter tokenVerificationFilter;

    @Autowired
    private VerificationCodeFilter verificationCodeFilter;

    @Autowired
    private RefererFilter refererFilter;

    @Autowired
    private AccessForbiddenHandler forbiddenHandler;

    @Autowired
    private AuthenticationHandler authenticationHandler;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 请求授权管理
        http.authorizeRequests()
                // 其他的请求都需要授权
                .antMatchers(commonConfig.getIgnoreUrl()).permitAll()
//                .antMatchers("/**").hasAnyRole("super_admin", "admin", "user")
                .antMatchers("/**").hasAnyRole("super_admin", "admin", "user")
                .antMatchers("/admin/**").hasAnyRole("super_admin", "admin")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(forbiddenHandler)
                .authenticationEntryPoint(authenticationHandler)
                .and()
                .csrf().disable()
                // 整合验证码过滤器
                .addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 整合token校验过滤器
                .addFilterBefore(tokenVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                // refererFilter过滤器
                .addFilterBefore(refererFilter, UsernamePasswordAuthenticationFilter.class)
                // 禁用springsecurity的本地存储，做无状态登录
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * 注入认证管理器
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {

        return super.authenticationManager();
    }
}
