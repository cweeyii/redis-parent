# redis-parent
redis client 访问：通过结合jedis和spring-data-redis包开发的一个简易的redis 集群访问接口

public interface IRedis<K,HK,HV> {
    void setHashObject(K key, Map<HK, HV> fieldValues, int expireSecond);

    void setHashObject(K key, Map<HK, HV> fieldValues);

    Map<HK, HV> getHashObject(K key, List<HK> fields);

    boolean expire(K key, int expireSeconds);

    void setObject(K key, HV value, int expireSecond);

    void setObject(K key, HV value);

    HV getObject(K key);
}
