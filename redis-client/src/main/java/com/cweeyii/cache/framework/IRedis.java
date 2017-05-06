package com.cweeyii.cache.framework;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public interface IRedis {
    <V> void hMultiSetObject(String key, Map<String, V> fieldValues);

    Long expire(String key, final int expireSeconds);

    <V> Map<String, V> hMultiGetObject(String key, List<String> fields);

    void setObject(final String key, int expireSecond, Object value);

    <V> V getObject(final String key);

    <V> void multiSetObject(Map<String, V> keyValue, int expireSecond);

    <V> Map<String, V> multiGetObject(List<String> keys);

    /**
     * 返回一个用来遍历所有与给定模式相匹配的key的迭代器
     *
     * @param pattern     用来匹配key的正则表达式
     * @param segmentSize segmentSize取值范围为{0,100}，表示每次迭代返回的key的数目，但不保证每次迭代所返回的元素数量一定是segmentSize，可能比指定的数量稍多一些
     * @return
     */
    Iterator<List<String>> scan(String pattern, int segmentSize);

    /**
     * 一次删除多个k-v对
     *
     * @param keys
     * @return 成功删除的k-v对的数目
     */
    long multiDelete(List<String> keys);
}
