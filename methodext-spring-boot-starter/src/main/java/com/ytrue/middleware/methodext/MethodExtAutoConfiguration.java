package com.ytrue.middleware.methodext;


import com.ytrue.middleware.methodext.aspect.DoMethodExtAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/9/15 17:45
 * @description MethodExtAutoConfiguration
 */
public class MethodExtAutoConfiguration {


    /**
     * 注册
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DoMethodExtAspect doMethodExtAspect() {
        return new DoMethodExtAspect();
    }
}
