package com.imzz.webchat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author: 黄训辉
 * Date: Created in 2018/2/7 14:19
 * Utils: Intellij Idea
 * Description: redis操作
 */
@Repository
public class RedisDao {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     */
    public void setKey(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param timeOut 过期时间
     * @param timeUnit 时间类型
     */
    public void setKey(String key, String value, Integer timeOut, TimeUnit timeUnit) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeOut, timeUnit);
    }

    /**
     * 获取缓存里面的参数
     * @param key 键
     * @return
     */
    public Object getValue(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * 设置hashMap
     * @param key 键
     * @param hashKey hashMap 键
     * @param item 需要存入对应hashMap的value
     */
    public void setHash(String key, String hashKey, Object item) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, item);
    }

    /**
     * 获取redis内的hashMap值
     * @param key redis键
     * @param hashKey hashMap键
     * @return 返回相应的数据
     */
    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 删除redis 里面的数据
     * @param key 键
     * @return 返回true 或者 false
     */
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

}
