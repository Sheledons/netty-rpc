package com.sheledon.common.netty.factory;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public class SingletonFactory {
    private static final Map<Class<?>,Object> SINGLETON_MAP = new ConcurrentHashMap<>();

    @SneakyThrows
    public static <T> T getInstance(Class<T> clazz){
        if (!SINGLETON_MAP.containsKey(clazz)){
            synchronized (clazz){
                if (!SINGLETON_MAP.containsKey(clazz)){
                    SINGLETON_MAP.put(clazz, clazz.newInstance());
                }
            }
        }
        return (T) SINGLETON_MAP.get(clazz);
    }
}
