package com.ytrue.middleware.db.router;

import com.ytrue.middleware.db.router.aspect.DbRouterAspect;
import com.ytrue.middleware.db.router.dynamic.DynamicDataSource;
import com.ytrue.middleware.db.router.properties.DbRouterProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ytrue
 * @date 2022/9/21 17:59
 * @description DataSourceAutoConfig
 */
@EnableConfigurationProperties(DbRouterProperties.class)
@AllArgsConstructor
public class DbRouterAutoConfiguration {

    private final DbRouterProperties dbRouterProperties;


    @Bean
    public DataSource dataSource() {
        // 创建数据源
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 获取配置
        List<String> list = dbRouterProperties.getList();
        for (String key : list) {
            DbRouterProperties.DbConfig dbConfig = dbRouterProperties.getDbConfig().get(key);
            targetDataSources.put(key, new DriverManagerDataSource(
                    dbConfig.getJdbcUrl(),
                    dbConfig.getUsername(),
                    dbConfig.getPassword()
            ));
        }

        // 设置数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }


    @Bean
    @ConditionalOnMissingBean
    public DbRouterAspect dbRouterAspect(){
        return  new DbRouterAspect(dbRouterProperties);
    }
}
