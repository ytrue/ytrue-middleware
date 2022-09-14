package com.ytrue.middleware.hystrix.valve;

import com.ytrue.middleware.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2022/9/14 14:52
 * @description IValveService
 */
public interface IValveService {
    Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable;
}
