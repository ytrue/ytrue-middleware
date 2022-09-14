package com.ytrue.middleware.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ytrue
 * @date 2022/9/14 15:26
 * @description MyHystrix
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyHystrix {

    /**
     * 默认超时时间
     * @return
     */
    int timeoutValue() default 1000;

    /**
     * 失败结果 JSON
     *
     * @return
     */
    String returnJson() default "";
}
