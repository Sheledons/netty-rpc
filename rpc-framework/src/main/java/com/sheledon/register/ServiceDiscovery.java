package com.sheledon.register;

import java.net.InetSocketAddress;

/**
 * @author Sheledon
 * @date 2021/11/14
 * @Version 1.0
 */
public interface ServiceDiscovery {
    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    InetSocketAddress discoverService(String serviceName);
}
