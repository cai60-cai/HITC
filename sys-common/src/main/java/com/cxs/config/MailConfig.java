package com.cxs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class MailConfig {
    private String host;
    private String username;
    private String password;
    private String phone;
    private String title;
}
