package com.sheledon.loadbalance.impl;

import com.sheledon.loadbalance.LoadBalance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询方式
 * @author Sheledon
 * @date 2021/11/21
 * @Version 1.0
 */
public class PollingBalance implements LoadBalance {

    private final Map<List<String>, AtomicInteger> pollingMap = new ConcurrentHashMap<>();

    @Override
    public String selectAddress(List<String> serviceList) {
        AtomicInteger atomicInteger = pollingMap.computeIfAbsent(serviceList, (key) -> {
            return new AtomicInteger(0);
        });
        if (atomicInteger.get()>= serviceList.size()){
            pollingMap.remove(serviceList);
            atomicInteger = pollingMap.computeIfAbsent(serviceList, (key) -> {
                return new AtomicInteger(0);
            });
        }
        int index = atomicInteger.getAndIncrement();
        return serviceList.get(index);
    }
}
