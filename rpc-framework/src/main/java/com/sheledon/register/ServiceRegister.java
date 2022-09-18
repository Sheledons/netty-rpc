package com.sheledon.register;

import java.net.InetSocketAddress;

/**
 * @author Sheledon
 * @date 2021/11/14
 * @Version 1.0
 */
public interface ServiceRegister {
    /**
     * 服务注册
     * @param serviceName
     * @param address
     */
    void registerService(String serviceName,InetSocketAddress address);
}
