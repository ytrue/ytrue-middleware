package com.ytrue.middleware.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author ytrue
 * @date 2022/9/27 09:50
 * @description DistributedRedisLock
 */
@Slf4j
public class DistributedRedisLock implements Lock {

    /**
     * redis模板
     */
    private StringRedisTemplate redisTemplate;

    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 锁过期时间
     */
    private long expire = 30;


    public DistributedRedisLock(StringRedisTemplate redisTemplate, String lockName, String uuid) {
        this.redisTemplate = redisTemplate;
        this.lockName = lockName;
        // uuid + 当前线程的id
        this.uuid = uuid + ":" + Thread.currentThread().getId();
    }


    /**
     * 获取锁
     */
    @Override
    public void lock() {
        this.tryLock();
    }

    /**
     * 可以打断的锁
     *
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("方法未实现");
    }

    /**
     * 尝试获取锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        try {
            return tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取锁失败", e);
        }
        return false;
    }

    /**
     * 指定时间获取锁
     *
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (time != -1) {
            this.expire = unit.toSeconds(time);
        }

        // lua 脚本
        String script = "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 " +
                "then " +
                "   redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                "   redis.call('expire', KEYS[1], ARGV[2]) " +
                "   return 1 " +
                "else " +
                "   return 0 " +
                "end";

        // key
        List<String> keys = new ArrayList<>();
        keys.add(lockName);

        // val
        Object[] values = {uuid, String.valueOf(expire)};

        while (!this.redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), keys, values)) {
            Thread.sleep(50);
        }

        // 加锁成功，返回之前，开启定时器自动续期
        this.renewExpire();
        return true;

    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0 " +
                "then " +
                "   return nil " +
                "elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 " +
                "then " +
                "   return redis.call('del', KEYS[1]) " +
                "else " +
                "   return 0 " +
                "end";

        Long flag = this.redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(lockName), uuid);

        if (flag == null) {
            throw new IllegalMonitorStateException("this lock doesn't belong to you!");
        }
    }

    /**
     * 略
     *
     * @return
     */
    @Override
    public Condition newCondition() {
        return null;
    }


    /**
     * 续期
     */
    private void renewExpire() {

        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 1 " +
                "then " +
                "   return redis.call('expire', KEYS[1], ARGV[2]) " +
                "else " +
                "   return 0 " +
                "end";


        // key
        List<String> keys = new ArrayList<>();
        keys.add(lockName);

        // val
        Object[] values = {uuid, String.valueOf(expire)};

        // 续期
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), keys, values)) {
                    renewExpire();
                }
            }
        }, this.expire * 1000 / 3);
    }
}
