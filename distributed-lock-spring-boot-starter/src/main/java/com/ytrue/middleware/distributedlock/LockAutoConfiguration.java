package com.ytrue.middleware.distributedlock;


import com.ytrue.middleware.distributedlock.aspect.DistributedRedisLockAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ytrue
 * @date 2022/9/27 10:16
 * @description LockAutoConfiguration
 */
public class LockAutoConfiguration {

    @Bean
    public DistributedLockClient distributedLockClient(StringRedisTemplate redisTemplate) {
        return new DistributedLockClient(redisTemplate);
    }

    @Bean
    public DistributedRedisLockAspect distributedRedisLockAspect(DistributedLockClient distributedLockClient) {
        return new DistributedRedisLockAspect(distributedLockClient);
    }
}
