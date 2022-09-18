package com.sheledon.common.netty.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sheledon
 */
public class IDUtils {
    private static AtomicLong atomicLong =
            new AtomicLong();
    public static String getRandomStringId(){
        return UUID.randomUUID()
                .toString()
                .replace("-","")
                .substring(0,10);
    }
    public static Long getRandomLongId(){
        return atomicLong.incrementAndGet();
    }
}
