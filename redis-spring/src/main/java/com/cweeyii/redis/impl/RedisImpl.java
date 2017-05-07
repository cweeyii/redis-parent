package com.cweeyii.redis.impl;


import com.cweeyii.redis.IRedis;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class RedisImpl<K, HK, HV> implements IRedis<K, HK, HV> {
    private static final int DEFAULT_EXPIRED_TIME=7*24*3600;
    @Resource
    private RedisTemplate<K, HV> redisTemplate;

    public void setHashObject(K key, Map<HK, HV> fieldValues, int expireSecond) {
        HashOperations<K, HK, HV> valueOperate = redisTemplate.opsForHash();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            valueOperate.putAll(key, fieldValues);
        }
        expire(key, expireSecond);
    }

    @Override
    public void setHashObject(K key, Map<HK, HV> fieldValues) {
        setHashObject(key,fieldValues, DEFAULT_EXPIRED_TIME);
    }

    public Map<HK, HV> getHashObject(K key, List<HK> fields) {
        HashOperations<K, HK, HV> valueOperate = redisTemplate.opsForHash();

        Map<HK, HV> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(fields)) {
            for (HK field : fields) {
                HV obj = valueOperate.get(key, field);
                map.put(field, obj);
            }
        }
        return map;
    }

    public boolean expire(K key, int expireSeconds) {
        return redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
    }

    public void setObject(K key, HV value, int expireSecond) {
        ValueOperations<K, HV> valueOperate = redisTemplate.opsForValue();
        valueOperate.set(key, value);
        redisTemplate.expire(key, expireSecond, TimeUnit.SECONDS);
    }

    @Override
    public void setObject(K key, HV value) {
        setObject(key,value,DEFAULT_EXPIRED_TIME);
    }

    public HV getObject(K key) {
        ValueOperations<K, HV> valueOperate = redisTemplate.opsForValue();
        return valueOperate.get(key);
    }
}
