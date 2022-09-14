package com.ytrue.middleware.hystrix.valve.impl;

import com.google.gson.Gson;
import com.netflix.hystrix.*;
import com.ytrue.middleware.hystrix.annotation.DoHystrix;
import com.ytrue.middleware.hystrix.valve.IValveService;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2022/9/14 14:53
 * @description HystrixValveImpl
 */
public class HystrixValveImpl extends HystrixCommand<Object> implements IValveService {


    private ProceedingJoinPoint jp;
    private Method method;
    private DoHystrix doHystrix;


    /*********************************************************************************************
     * 置HystrixCommand的属性
     * GroupKey：            该命令属于哪一个组，可以帮助我们更好的组织命令。
     * CommandKey：          该命令的名称
     * ThreadPoolKey：       该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称。
     * CommandProperties：   该命令的一些设置，包括断路器的配置，隔离策略，降级设置，以及一些监控指标等。
     * ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等
     *********************************************************************************************/
    public HystrixValveImpl() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GovernKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GovernThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10))
        );

    }

    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable {

        this.jp = jp;
        this.method = method;
        this.doHystrix = doHystrix;

        // 设置熔断超时时间
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(doHystrix.timeoutValue()));

        return this.execute();
    }

    @Override
    protected Object run() throws Exception {
        try {
            return jp.proceed();
        } catch (Throwable throwable) {
            return null;
        }

    }

    /**
     * 降级处理
     *
     * @return
     */
    @Override
    protected Object getFallback() {
        return new Gson().fromJson(doHystrix.returnJson(), method.getReturnType());
    }
}
