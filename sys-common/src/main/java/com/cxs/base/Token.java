package com.cxs.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

  
@Data
public class Token {
    private UserSubject user;

    private String token;

    @JSONField(serialize = false, format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issuedAtTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;
}
