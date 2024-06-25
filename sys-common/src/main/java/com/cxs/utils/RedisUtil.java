package com.cxs.utils;

import com.cxs.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class RedisUtil {

    private static final Long DEFAULT_TIME = 30L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CommonConfig commonConfig;

    public void set(String key, Object value){
        set(key, value, DEFAULT_TIME, TimeUnit.MINUTES);
    }

    public void set(String key, Object value, Long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(getKey(key), value, time, timeUnit);
    }

    /**
     * 加锁
     * @param key
     * @param timeStamp
     * @return
     */
    public Boolean lock(String key, String timeStamp){
        if (redisTemplate.opsForValue().setIfAbsent(getKey(key), timeStamp)) {
            return true;
        }
        String currentLock = (String) redisTemplate.opsForValue().get(getKey(key));
        if (StringUtils.hasLength(currentLock) && Long.parseLong(currentLock) < System.currentTimeMillis()) {
            String preLock = (String) redisTemplate.opsForValue().getAndSet(getKey(key), timeStamp);

            if (StringUtils.hasLength(preLock) && preLock.equals(currentLock)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param timeStamp
     */
    public void unLock(String key, String timeStamp){
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(getKey(key));
            if (StringUtils.hasLength(currentValue) && currentValue.equals(timeStamp)) {
                redisTemplate.opsForValue().getOperations().delete(getKey(key));
            }
        } catch (Exception e) {
            log.error("解锁异常");
        }
    }

    public String getString(String key) {
        Object object = redisTemplate.opsForValue().get(getKey(key));
        if (ObjectUtils.isEmpty(object)) {
            return null;
        } else {
            return object.toString();
        }
    }

    public Object getValue(String key) {
        Object object = redisTemplate.opsForValue().get(getKey(key));
        return object;
    }

    public Long getExpire(String key) {
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(getKey(key));
        return expire;
    }

    public void removeKey(String cacheKey) {
        redisTemplate.delete(getKey(cacheKey));
    }

    public String getCacheKey(String prefix, String... args) {
        if (ObjectUtils.isEmpty(args) || args.length <= 0) {
            return prefix;
        }
        StringJoiner joiner = new StringJoiner(":");
        for (String arg : args) {
            if (StringUtils.hasLength(arg)) {
                joiner.add(arg);
            }
        }
        return new StringBuilder()
                .append(prefix)
                .append(joiner.toString()).toString();
    }

    private String getKey(String key){
        StringBuilder sb = new StringBuilder();
        sb.append(commonConfig.getApplication())
                .append(":")
                .append(key);
        return sb.toString();
    }
}
