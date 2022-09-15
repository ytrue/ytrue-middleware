package com.ytrue.middleware.methodext.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ytrue
 * @date 2022/9/15 17:43
 * @description DoMethodExt
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoMethodExt {

    /**
     * 方法
     *
     * @return
     */
    String method() default "";

    /**
     * 返回json
     *
     * @return
     */
    String returnJson() default "";

}
