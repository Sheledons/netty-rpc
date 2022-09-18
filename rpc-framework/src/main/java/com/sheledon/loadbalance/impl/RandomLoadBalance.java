package com.sheledon.loadbalance.impl;

import com.sheledon.loadbalance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @author Sheledon
 * @date 2021/11/21
 * @Version 1.0
 */
public class RandomLoadBalance implements LoadBalance {
    private final Random baseRandom = new Random(Integer.MAX_VALUE);
    @Override
    public String selectAddress(List<String> serviceList) {
        Random random = new Random(baseRandom.nextInt());
        int index = random.nextInt(serviceList.size());
        return serviceList.get(index);
    }

}
