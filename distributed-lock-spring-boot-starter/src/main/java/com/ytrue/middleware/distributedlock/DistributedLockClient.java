package com.ytrue.middleware.distributedlock;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

/**
 * @author ytrue
 * @date 2022/9/27 10:15
 * @description DistributedLockClient
 */
public class DistributedLockClient {

    private StringRedisTemplate redisTemplate;

    private String uuid;

    public DistributedLockClient(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.uuid = UUID.randomUUID().toString();
    }


    public DistributedRedisLock getRedisLock(String lockName) {
        return new DistributedRedisLock(redisTemplate, lockName, uuid);
    }
}
