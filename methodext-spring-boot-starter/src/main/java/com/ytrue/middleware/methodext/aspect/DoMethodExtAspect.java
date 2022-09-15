package com.ytrue.middleware.methodext.aspect;

import com.google.gson.Gson;
import com.ytrue.middleware.methodext.annotation.DoMethodExt;
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
 * @date 2022/9/15 17:43
 * @description DoMethodExtAspect
 */
@Aspect
public class DoMethodExtAspect {

    private final static String BOOLEAN = "boolean";

    @Pointcut("@annotation(com.ytrue.middleware.methodext.annotation.DoMethodExt)")
    public void aopPoint() {
    }

    @Around(value = "aopPoint()")
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable {
        // 获取内容
        Method method = getMethod(jp);
        DoMethodExt doMethodExt = method.getAnnotation(DoMethodExt.class);
        // 获取拦截方法
        String methodName = doMethodExt.method();
        // 功能处理
        Method methodExt = getClass(jp).getMethod(methodName, method.getParameterTypes());
        Class<?> returnType = methodExt.getReturnType();

        // 判断方法返回类型
        if (!BOOLEAN.equals(returnType.getName())) {
            throw new RuntimeException("annotation @DoMethodExt set method：" + methodName + " returnType is not boolean");
        }
        // 拦截判断正常，继续
        boolean invoke = (boolean) methodExt.invoke(jp.getThis(), jp.getArgs());
        // 返回结果
        return invoke ? jp.proceed() : new Gson().fromJson(doMethodExt.returnJson(), method.getReturnType());
    }

    /**
     * 获取方法
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

    /**
     * 获取clazz
     *
     * @param jp
     * @return
     * @throws NoSuchMethodException
     */
    private Class<? extends Object> getClass(JoinPoint jp) throws NoSuchMethodException {
        return jp.getTarget().getClass();
    }

}
