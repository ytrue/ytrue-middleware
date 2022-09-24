package com.ytrue.middleware.db.router.aspect;

import com.ytrue.middleware.db.router.DbContextHolder;
import com.ytrue.middleware.db.router.annotation.DbRouter;
import com.ytrue.middleware.db.router.properties.DbRouterProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author ytrue
 * @date 2022/9/21 17:55
 * @description DbRouterAspect
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class DbRouterAspect {

    private final DbRouterProperties dbRouterProperties;

    @Pointcut("@annotation(com.ytrue.middleware.db.router.annotation.DbRouter)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(dbRouter)")
    public Object doRouter(ProceedingJoinPoint jp, DbRouter dbRouter) throws Throwable {
        // 获取key
        String dbKey = dbRouter.key();
        // 判断key是否为空
        if (StringUtils.isBlank(dbKey)) {
            throw new RuntimeException("annotation DBRouter key is null！");
        }
        // 获取对应的值
        String dbKeyAttr = getAttrValue(dbKey, jp.getArgs());
        // 库数 * 表数
        int size = dbRouterProperties.getDbCount() * dbRouterProperties.getTbCount();
        // 扰动函数
        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));
        // 库表索引
        int dbIdx = idx / dbRouterProperties.getTbCount() + 1;
        int tbIdx = idx - dbRouterProperties.getTbCount() * (dbIdx - 1) + 1;
        // 设置到 ThreadLocal
        DbContextHolder.setDBKey(String.format("%02d", dbIdx));
        DbContextHolder.setTBKey(String.format("%02d", tbIdx));
        log.info("数据库路由 method：{} dbIdx：{} tbIdx：{}", getMethod(jp).getName(), dbIdx, tbIdx);

        // 返回结果
        try {
            return jp.proceed();
        } finally {
            DbContextHolder.clearDBKey();
            DbContextHolder.clearTBKey();
        }
    }

    /**
     * 获取方法
     *
     * @param jp
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    /**
     * 获取对象指定的参数
     *
     * @param attr
     * @param args
     * @return
     */
    public String getAttrValue(String attr, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                filedValue = BeanUtils.getProperty(arg, attr);
            } catch (Exception e) {
                log.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }
}
