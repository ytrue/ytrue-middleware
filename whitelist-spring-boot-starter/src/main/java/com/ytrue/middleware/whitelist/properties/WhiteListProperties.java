package com.ytrue.middleware.whitelist.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author ytrue
 * @date 2022/9/11 21:18
 * @description WhiteListProperties
 */
@Data
@ConfigurationProperties("ytrue.whitelist")
public class WhiteListProperties {

    /**
     * # 白名单用户
     */
    private List<String> users;

}
