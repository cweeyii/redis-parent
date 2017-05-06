package com.cweeyii.cache.framework.impl;

import com.cweeyii.cache.framework.IRedis;
import com.cweeyii.cache.framework.iterator.ScanIterator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.terracotta.quartz.collections.SerializationHelper;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Component
public class RedisImpl implements IRedis {
    @Resource
    private JedisPool jedisPool;

    public <V> void hMultiSetObject(String key, Map<String, V> fieldValues) {
        Jedis jedis = jedisPool.getResource();
        if (!CollectionUtils.isEmpty(fieldValues)) {
            for (Map.Entry<String, V> entry : fieldValues.entrySet()) {
                String field = entry.getKey();
                try {
                    String value = SerializationHelper.serializeToString(entry.getValue());
                    jedis.hset(key, field, value);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public Long expire(String key, int expireSeconds) {
        Jedis jedis = jedisPool.getResource();
        return jedis.expire(key, expireSeconds);
    }


    public <V> Map<String, V> hMultiGetObject(String key, List<String> fields) {
        Jedis jedis = jedisPool.getResource();
        Map<String, V> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(fields)) {
            for (String field : fields) {
                try {
                    String value = jedis.hget(key, field);
                    if (value != null) {
                        Object obj = SerializationHelper.deserializeFromString(value);
                        map.put(field, (V) obj);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public void setObject(String key, int expireSecond, Object value) {

        try {
            Jedis jedis = jedisPool.getResource();
            String strValue = SerializationHelper.serializeToString(value);
            jedis.set(key, strValue);
            jedis.expire(key, expireSecond);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <V> V getObject(String key) {
        V obj = null;
        try {
            Jedis jedis = jedisPool.getResource();
            String strValue = jedis.get(key);
            if (strValue != null) {
                obj = (V) SerializationHelper.deserializeFromString(strValue);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public <V> void multiSetObject(Map<String, V> keyValue, int expireSecond) {
        Jedis jedis = jedisPool.getResource();
        if (!CollectionUtils.isEmpty(keyValue)) {
            for (Map.Entry<String, V> entry : keyValue.entrySet()) {
                String key = entry.getKey();
                try {
                    String value = SerializationHelper.serializeToString(entry.getValue());
                    jedis.set(key, value);
                    jedis.expire(key, expireSecond);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public <V> Map<String, V> multiGetObject(List<String> keys) {
        Jedis jedis = jedisPool.getResource();
        Map<String, V> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(keys)) {
            for (String key : keys) {
                try {
                    String value = jedis.get(key);
                    if (value != null) {
                        Object obj = SerializationHelper.deserializeFromString(value);
                        map.put(key, (V) obj);
                    } else {
                        map.put(key, null);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public Iterator<List<String>> scan(String pattern, int segmentSize) {
        Jedis jedis = jedisPool.getResource();
        return new ScanIterator(jedis, pattern, segmentSize);
    }

    public long multiDelete(List<String> keys) {
        long count = 0;
        Jedis jedis = jedisPool.getResource();
        if (!CollectionUtils.isEmpty(keys)) {
            for (String key : keys) {
                count += jedis.del(key);
            }
        }
        return count;
    }
}
