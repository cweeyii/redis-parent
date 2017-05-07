package com.cweeyii.redis;

import java.util.List;
import java.util.Map;


public interface IRedis<K,HK,HV> {
    void setHashObject(K key, Map<HK, HV> fieldValues, int expireSecond);

    void setHashObject(K key, Map<HK, HV> fieldValues);

    Map<HK, HV> getHashObject(K key, List<HK> fields);

    boolean expire(K key, int expireSeconds);

    void setObject(K key, HV value, int expireSecond);

    void setObject(K key, HV value);

    HV getObject(K key);
}
