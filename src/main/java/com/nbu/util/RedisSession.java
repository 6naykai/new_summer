package com.nbu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


public class RedisSession {
    private Integer EXPIRE_TIME = 1;
    @Autowired
    @Qualifier("SimpleJsonUtil")
    private JsonUtil jsonUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    public void StringSet(String key,Object value) {
        redisTemplate.opsForValue().set(key,jsonUtil.ObjectToJsonString(value),EXPIRE_TIME, TimeUnit.DAYS);
    }
    public Object StringGet(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void StringDel(String key) {
        redisTemplate.expire(key,0,TimeUnit.SECONDS);
    }

}
