package com.ytrue.middleware.hystrix.aspect;

import com.google.gson.Gson;
import com.ytrue.middleware.hystrix.annotation.MyHystrix;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author ytrue
 * @date 2022/9/14 15:26
 * @description MyHystrixAspect
 */
@Aspect
@Slf4j
@Component
public class MyHystrixAspect {

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Pointcut("@annotation(com.ytrue.middleware.hystrix.annotation.MyHystrix)")
    public void aopPoint() {
    }

    @Around(value = "aopPoint()&&@annotation(hystrix)")
    public Object doPointCut(ProceedingJoinPoint joinPoint, MyHystrix hystrix) throws Throwable {
        // 获取超时时间
        int timeout = hystrix.timeoutValue();
        // 执行结果
        Future<ResultWrapper> future = executorService.submit(() -> {
            ResultWrapper resultWrapper = new ResultWrapper();
            try {
                // 执行目标方法,这里要多异常做处理
                resultWrapper.setResult(joinPoint.proceed());
            } catch (Throwable e) {
                resultWrapper.setThrowable(e);
            }
            return resultWrapper;
        });

        ResultWrapper result;
        try {
            result = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // 清空任务
            future.cancel(true);
            // result = invokeFallback(joinPoint, hystrix.fallback());
            return new Gson().fromJson(hystrix.returnJson(), getMethod(joinPoint).getReturnType());
        }

        // 判断有没有异常
        if (null != result.getThrowable()) {
            throw result.getThrowable();
        }

        return result.getResult();
    }


    /**
     * 获取Method
     *
     * @param jp
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


    @Data
    private static class ResultWrapper {
        private Object result;
        private Throwable throwable;
    }


    /**
     * 调用fallback方法
     *
     * @param joinPoint
     * @param fallback
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invokeFallback(ProceedingJoinPoint joinPoint, String fallback) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 得到fallback方法
        Method fallbackMethod = joinPoint.getTarget().getClass().getMethod(fallback);
        fallbackMethod.setAccessible(true);
        // 完成反射调用
        return fallbackMethod.invoke(joinPoint.getTarget());
    }
}
