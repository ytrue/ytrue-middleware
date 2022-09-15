package com.ytrue.middleware.ratelimiter;

import com.ytrue.middleware.ratelimiter.aspect.DoRateLimiterAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/9/15 17:32
 * @description RateLimiterAutoConfiguration
 */
public class RateLimiterAutoConfiguration {


    /**
     * 注册
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DoRateLimiterAspect odRateLimiterAspect() {
        return new DoRateLimiterAspect();
    }

}
