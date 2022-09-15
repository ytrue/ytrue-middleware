package com.ytrue.middleware.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ytrue
 * @date 2022/9/15 17:05
 * @description Constants
 */

public class Constants {
    public static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
}
