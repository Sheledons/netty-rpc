package com.sheledon.provider;

import com.sheledon.config.RpcServiceConfig;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public interface ServiceProvider {
    void addService(RpcServiceConfig serviceConfig);
    Object getService(String serviceName);
    void publishService(RpcServiceConfig serviceConfig);
}
