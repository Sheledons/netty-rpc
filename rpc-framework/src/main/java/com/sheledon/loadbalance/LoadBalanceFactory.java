package com.sheledon.loadbalance;

import com.sheledon.loadbalance.impl.PollingBalance;
import com.sheledon.loadbalance.impl.RandomLoadBalance;
import lombok.SneakyThrows;

import java.util.HashMap;

/**
 * @author Sheledon
 * @date 2022/3/14
 * @Version 1.0
 */
public class LoadBalanceFactory {

    private static final HashMap<String,LoadBalance> loadBalanceMap = new HashMap<>();
    private static final HashMap<String,Class<? extends LoadBalance>> loadBalanceClassMap = new HashMap<>();

    static {
        loadBalanceClassMap.put(Constants.POLLING_BALANCE,PollingBalance.class);
        loadBalanceClassMap.put(Constants.RANDOM_BALANCE,RandomLoadBalance.class);
    }

    @SneakyThrows
    public static LoadBalance getLoadBalance(String name){
        LoadBalance loadBalance = loadBalanceMap.get(name);
        if (loadBalance==null && loadBalanceClassMap.containsKey(name)){
            Class<? extends LoadBalance> clazz = loadBalanceClassMap.get(name);
            loadBalance = loadBalanceMap.computeIfAbsent(name,key->{
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
        return loadBalance;
    }

}
