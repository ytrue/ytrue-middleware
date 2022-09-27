package com.ytrue.middleware.lock.aspect;

import com.ytrue.middleware.lock.DistributedLockClient;
import com.ytrue.middleware.lock.annotation.DistributedRedisLock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author ytrue
 * @date 2022/9/27 10:21
 * @description DistributedRedisLockAspect
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class DistributedRedisLockAspect {

    private final DistributedLockClient distributedLockClient;

    @Pointcut("@annotation(com.ytrue.middleware.lock.annotation.DistributedRedisLock)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(distributedRedisLock)")
    public Object doRouter(ProceedingJoinPoint jp, DistributedRedisLock distributedRedisLock) throws Throwable {
        // 获取key
        String lockName = distributedRedisLock.lockName();

        // 判断key是否为空
        if (StringUtils.isBlank(lockName)) {
            throw new RuntimeException("annotation DistributedRedisLock lockName is null！");
        }

        com.ytrue.middleware.lock.DistributedRedisLock redisLock = this.distributedLockClient.getRedisLock(lockName);
        redisLock.lock();
        try {
            return jp.proceed();
        } finally {
            redisLock.unlock();
        }
    }
}
