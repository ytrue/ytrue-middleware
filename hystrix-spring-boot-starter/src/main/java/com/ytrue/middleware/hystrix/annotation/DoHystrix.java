package com.ytrue.middleware.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ytrue
 * @date 2022/9/14 14:49
 * @description DoHystrix
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoHystrix {

    /**
     * 失败结果 JSON
     *
     * @return
     */
    String returnJson() default "";

    /**
     * 超时时间
     *
     * @return
     */
    int timeoutValue() default 0;

}
