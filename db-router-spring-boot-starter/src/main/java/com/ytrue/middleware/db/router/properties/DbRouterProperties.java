package com.ytrue.middleware.db.router.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ytrue
 * @date 2022/9/21 17:42
 * @description DbRouterProperties
 */
@Data
@ConfigurationProperties(prefix = "ytrue.db-router")
public class DbRouterProperties {

    /**
     * 分库数
     */
    private int dbCount;

    /**
     * 分表数
     */
    private int tbCount;

    /**
     * 可以路由的key
     */
    private List<String> list;

    /**
     * 数据库分组
     **/
    private Map<String, DbConfig> dbConfig = new LinkedHashMap<>();

    @Data
    public static class DbConfig {
        private String driverClassName;
        private String jdbcUrl;
        private String username;
        private String password;
    }
}
