package com.sheledon.loadbalance;

import java.util.List;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public interface LoadBalance {
    String selectAddress(List<String> serviceList);
}
