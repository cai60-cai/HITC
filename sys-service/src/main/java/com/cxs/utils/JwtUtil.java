package com.cxs.utils;

import com.alibaba.fastjson.JSON;
import com.cxs.base.Token;
import com.cxs.base.UserSubject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    @Value("${auth.jwt.signingKey}")
    private String signingKey;

    @Value("${auth.jwt.validityTime:30}")
    private Integer validityTime;

    /**
     * 获得token
     *
     * @param user
     * @return
     */
    public String generateToken(UserSubject user) {
        Map<String, Object> hashMap = JSON.parseObject(JSON.toJSONString(user), HashMap.class);
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + validityTime * 60 * 1000);
        return Jwts.builder()
                .setId(user.getId() + "")
                .addClaims(hashMap)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, signingKey).compact();
    }

    /**
     * 解析token
     *
     * @param token
     * @return SignatureException 无效
     * ExpiredJwtException 过期
     */
    public Token parseToken(String token) {
        UserSubject user = new UserSubject();
        Token result = new Token();
        Claims claims = null;
        claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
        if (ObjectUtils.isEmpty(claims)) {
            return null;
        }
        result.setToken(token);
        result.setIssuedAtTime(LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()));
        result.setExpirationTime(LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()));
        user = JSON.parseObject(JSON.toJSONString(claims), UserSubject.class);
        result.setUser(user);
        return result;
    }

    /**
     * 判断是否过期
     *
     * @param token
     * @return
     */
    public boolean validTokenIssued(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
