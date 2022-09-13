package com.ytrue.middleware.whitelist;

import com.ytrue.middleware.whitelist.aspect.WhiteListAspect;
import com.ytrue.middleware.whitelist.properties.WhiteListProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ytrue
 * @date 2022/9/11 21:18
 * @description WhiteListAutoConfigure
 */
@EnableConfigurationProperties(WhiteListProperties.class)
public class WhiteListAutoConfiguration {


    /**
     * 注册
     *
     * @param whiteListProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public WhiteListAspect whiteListAspect(WhiteListProperties whiteListProperties) {
        return new WhiteListAspect(whiteListProperties);
    }
}
