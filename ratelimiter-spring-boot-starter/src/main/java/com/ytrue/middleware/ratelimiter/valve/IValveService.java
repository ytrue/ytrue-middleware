package com.ytrue.middleware.ratelimiter.valve;

import com.ytrue.middleware.ratelimiter.annotation.DoRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2022/9/15 17:02
 * @description IValveService
 */
public interface IValveService {

    /**
     * 访问
     *
     * @param jp
     * @param method
     * @param doRateLimiter
     * @param args
     * @return
     * @throws Throwable
     */
    Object access(ProceedingJoinPoint jp, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable;
}
