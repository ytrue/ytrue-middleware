package com.ytrue.middleware.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ytrue
 * @date 2022/9/15 16:59
 * @description DoRateLimiter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoRateLimiter {
    /**
     * 限流许可量
     *
     * @return
     */
    double permitsPerSecond() default 0D;

    /**
     * 失败结果 JSON
     *
     * @return
     */
    String returnJson() default "";

}
