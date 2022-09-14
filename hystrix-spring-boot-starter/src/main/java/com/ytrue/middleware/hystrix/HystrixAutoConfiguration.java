package com.ytrue.middleware.hystrix;

import com.ytrue.middleware.hystrix.aspect.DoHystrixAspect;
import com.ytrue.middleware.hystrix.aspect.MyHystrixAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/9/11 21:18
 * @description WhiteListAutoConfigure
 */
public class HystrixAutoConfiguration {

    /**
     * 注册
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DoHystrixAspect doHystrixAspect() {
        return new DoHystrixAspect();
    }

    /**
     * 注册
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MyHystrixAspect myHystrixAspect() {
        return new MyHystrixAspect();
    }
}
