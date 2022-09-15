package com.ytrue.middleware.ratelimiter.valve.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.ytrue.middleware.ratelimiter.Constants;
import com.ytrue.middleware.ratelimiter.annotation.DoRateLimiter;
import com.ytrue.middleware.ratelimiter.valve.IValveService;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2022/9/15 17:02
 * @description RateLimiterValve
 */
public class RateLimiterValve implements IValveService {

    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable {

        // 判断是否开启
        if (0 == doRateLimiter.permitsPerSecond()) {
            return jp.proceed();
        }

        String clazzName = jp.getTarget().getClass().getName();
        String methodName = method.getName();

        String key = clazzName + "." + methodName;

        if (null == Constants.rateLimiterMap.get(key)) {
            Constants.rateLimiterMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
        }

        RateLimiter rateLimiter = Constants.rateLimiterMap.get(key);
        if (rateLimiter.tryAcquire()) {
            return jp.proceed();
        }

        return new Gson().fromJson(doRateLimiter.returnJson(), method.getReturnType());
    }
}
