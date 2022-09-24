package com.ytrue.middleware.db.router.dynamic;

import com.ytrue.middleware.db.router.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author ytrue
 * @date 2022/9/21 17:45
 * @description 继承抽象类AbstractRoutingDataSource，通过扩展这个类实现根据不同的请求切换数据源。
 * https://www.jianshu.com/p/edc282e6bbb9 参考文章
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 通过determineCurrentLookupKey()方法获取一个key，
     * 通过key从resolvedDataSources中获取数据源DataSource对象。
     * determineCurrentLookupKey()是个抽象方法，需要继承AbstractRoutingDataSource的类实现；
     * 而resolvedDataSources是一个Map<Object, DataSource>，里面应该保存当前所有可切换的数据源。
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        Object data = "db" + DbContextHolder.getDBKey();
        System.out.println(123);

        return data;
    }
}
