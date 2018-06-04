package com.wing.core.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author 夏飞
 * 线程上下文
 */
public class ThreadLocalUtils {

    private static final LoadingCache<String,ThreadLocal<Object>> THREAD_LOCAL_CACHE=CacheBuilder.newBuilder().build(new CacheLoader<String, ThreadLocal<Object>>() {
        @Override
        public ThreadLocal<Object> load(String key) {
            return new ThreadLocal<>();
        }
    });

    public static Object get(String key) {
        return THREAD_LOCAL_CACHE.getUnchecked(key).get();
    }

    /**
     * 有由于一个线程不存在同时一时刻 两次进入set方法，所以不用线程安全处理
     */
    public static void set(String key, Object value) {
        THREAD_LOCAL_CACHE.getUnchecked(key).set(value);
    }


}