package com.ytrue.middleware.whitelist;


import com.ytrue.middleware.whitelist.annotation.DoWhiteList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/9/11 21:19
 * @description DoJoinPoint
 */

@Aspect
@Slf4j
@AllArgsConstructor
public class DoJoinPoint {

    @Pointcut("@annotation(com.ytrue.middleware.whitelist.annotation.DoWhiteList)")
    public void aopPoint() {
    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解
        Method method = getMethod(joinPoint);
        DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);

        return null;
    }

    /**
     * 获取方法
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


    /**
     * 获取属性值
     *
     * @param filed
     * @param args
     * @return
     */
    private String getFiledValue(String filed, Object[] args) {

        return null;
    }
}
