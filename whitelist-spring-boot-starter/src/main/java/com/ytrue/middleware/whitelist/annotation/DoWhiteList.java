package com.ytrue.middleware.whitelist.annotation;

import java.lang.annotation.*;

/**
 * @author ytrue
 * @date 2022/9/11 21:15
 * @description DoWhiteList
 * <p>
 * 如果一个类用上了@Inherited修饰的注解，那么其子类也会继承这个注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface DoWhiteList {

    /**
     * 对比的key
     *
     * @return
     */
    String key() default "";

    /**
     * 方法拦截后的返回信息
     *
     * @return
     */
    String returnJson() default "";

}
