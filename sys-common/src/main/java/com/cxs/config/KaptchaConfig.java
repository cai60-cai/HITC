package com.cxs.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

  
@Configuration
public class KaptchaConfig {

    private static final String CHAR_STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_LENGTH = "4";

    @Bean
    public Producer captcha() {
        // 配置图形验证码的基本参数
        Properties properties = new Properties();
        // 字符集
        properties.setProperty("kaptcha.textproducer.char.string", CHAR_STR);
        // 字符长度
        properties.setProperty("kaptcha.textproducer.char.length", CHAR_LENGTH);
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
