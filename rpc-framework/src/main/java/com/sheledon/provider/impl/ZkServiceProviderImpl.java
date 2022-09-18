package com.sheledon.provider.impl;

import com.sheledon.config.RpcServiceConfig;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.provider.ServiceProvider;
import com.sheledon.provider.impl.exception.ServiceException;
import com.sheledon.register.ServiceRegister;
import com.sheledon.register.zk.ZkServiceRegister;
import com.sheledon.transport.netty.server.RpcServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供类
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public class ZkServiceProviderImpl implements ServiceProvider {
    private final ServiceRegister SERVICE_REGISTER;
    private final Set<String> REGISTERED_SERVICE_SET;
    private final Map<String,Object> REGISTERED_SERVICE_MAP ;
    private final Map<String,RpcServiceConfig> SERVICE_CONFIG_MAP;

    public ZkServiceProviderImpl() {
        REGISTERED_SERVICE_MAP = new ConcurrentHashMap<>();
        REGISTERED_SERVICE_SET = ConcurrentHashMap.newKeySet();
        SERVICE_REGISTER = SingletonFactory.getInstance(ZkServiceRegister.class);
        SERVICE_CONFIG_MAP = new ConcurrentHashMap<>();
    }

    @Override
    public void addService(RpcServiceConfig serviceConfig) {
        String rpcServiceName = serviceConfig.getRpcServiceName();
        if (REGISTERED_SERVICE_SET.contains(rpcServiceName)){
            throw new ServiceException("service : ["+rpcServiceName+"] repeated");
        }
        REGISTERED_SERVICE_SET.add(rpcServiceName);
        REGISTERED_SERVICE_MAP.put(rpcServiceName, serviceConfig.getService());
        SERVICE_CONFIG_MAP.put(rpcServiceName,serviceConfig);
    }

    @Override
    public Object getService(String serviceName) {
        if (!REGISTERED_SERVICE_SET.contains(serviceName)){
            throw  new ServiceException("service : " + "["+serviceName+"] not exist");
        }
        return REGISTERED_SERVICE_MAP.get(serviceName);
    }

    /**
     * @param serviceConfig
     */
    @Override
    public void publishService(RpcServiceConfig serviceConfig) {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            this.addService(serviceConfig);
            SERVICE_REGISTER.registerService(serviceConfig.getRpcServiceName(),
                                             new InetSocketAddress(hostAddress,RpcServer.SERVER_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new ServiceException("unknown server host");
        }
    }
}
