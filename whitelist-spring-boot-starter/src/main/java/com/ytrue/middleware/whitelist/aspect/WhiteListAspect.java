package com.ytrue.middleware.whitelist.aspect;


import com.google.gson.Gson;
import com.ytrue.middleware.whitelist.annotation.DoWhiteList;
import com.ytrue.middleware.whitelist.properties.WhiteListProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ytrue
 * @date 2022/9/11 21:19
 * @description DoJoinPoint
 */

@Aspect
@Slf4j
@AllArgsConstructor
public class WhiteListAspect {

    private final WhiteListProperties whiteListProperties;

    @Pointcut("@annotation(com.ytrue.middleware.whitelist.annotation.DoWhiteList)")
    public void aopPoint() {
    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解
        Method method = getMethod(joinPoint);
        DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);

        // 获取字段值
        String keyValue = getFiledValue(whiteList.key(), joinPoint.getArgs());

        // 没有匹配就放行
        if (null == keyValue || "".equals(keyValue)) {
            return joinPoint.proceed();
        }

        // 获取白名单
        List<String> users = whiteListProperties.getUsers();

        // 白名单过滤
        for (String user : users) {
            if (keyValue.equals(user)) {
                return joinPoint.proceed();
            }
        }

        // 拦截处理

        return returnObject(whiteList, method);
    }


    /**
     * 返回对象
     *
     * @param whiteList
     * @param method
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Object returnObject(DoWhiteList whiteList, Method method) throws IllegalAccessException, InstantiationException {
        Class<?> returnType = method.getReturnType();
        String returnJson = whiteList.returnJson();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }

        return new Gson().fromJson(returnJson, returnType);
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
        String filedValue = null;
        // 循环获取
        for (Object arg : args) {
            try {
                if (null == filedValue || "".equals(filedValue)) {
                    filedValue = BeanUtils.getProperty(arg, filed);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return filedValue;
    }
}
