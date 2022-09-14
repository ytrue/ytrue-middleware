package com.ytrue.middleware.hystrix.aspect;

import com.ytrue.middleware.hystrix.annotation.DoHystrix;
import com.ytrue.middleware.hystrix.valve.IValveService;
import com.ytrue.middleware.hystrix.valve.impl.HystrixValveImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2022/9/14 15:01
 * @description DoHystrixAspect
 */
@Aspect
public class DoHystrixAspect {

    @Pointcut("@annotation(com.ytrue.middleware.hystrix.annotation.DoHystrix)")
    public void aopPoint() {
    }


    /**
     * @param jp
     * @param doGovern
     * @return
     * @throws Throwable
     * @Around("aopPoint() && @annotation(doGovern)") ，
     * 这块的处理是本章节新增的，一般在方法入参中并没有直接提供自定义注解的获取，而是通过类和方法再反向找出来。
     * 而直接通过方法入参的方式可以更加方便的拿到注解，处理起来也更优雅
     */
    @Around("aopPoint() && @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint jp, DoHystrix doGovern) throws Throwable {
        IValveService valveService = new HystrixValveImpl();
        return valveService.access(jp, getMethod(jp), doGovern, jp.getArgs());
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

}
