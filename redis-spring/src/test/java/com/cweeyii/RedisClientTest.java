package com.cweeyii;


import com.cweeyii.redis.DateUtils;
import com.cweeyii.redis.IRedis;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenyi on 17/5/7.
 * Email:caowenyi@meituan.com
 */
public class RedisClientTest extends BaseTest {
    @Resource
    private IRedis iRedis;

    @Test
    public void testSetObject() {
        for (int i = 0; i < 1000; i++) {
            String key = "firstKey" + i;
            String value = "firstValue" + i;
            iRedis.setObject(key, value, DateUtils.getRandomExpireDays(21, 7) * 24 * 3600);
        }

        for (int i = 0; i < 1000; i++) {
            String key = "firstKey" + i;
            String value = (String) iRedis.getObject(key);
            System.out.println(value);
        }
    }

    @Test
    public void testHash() {
        Map<String, Integer> multiValueMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String hkey = "firstKey" + i;
            multiValueMap.put(hkey, i);
        }
        String key = "key";
        iRedis.setHashObject(key, multiValueMap);
        List<String> hKeyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            hKeyList.add("firstKey" + i);
        }
        Map<String, Integer> valueMap = iRedis.getHashObject(key, hKeyList);
        System.out.print(valueMap);
    }
}
