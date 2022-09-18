package com.sheledon.register.zk;

import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.context.RpcContext;
import com.sheledon.loadbalance.LoadBalance;
import com.sheledon.loadbalance.LoadBalanceFactory;
import com.sheledon.loadbalance.impl.RandomLoadBalance;
import com.sheledon.provider.impl.exception.ServiceException;
import com.sheledon.register.ServiceDiscovery;
import com.sheledon.register.zk.utils.CuratorUtils;
import com.sheledon.transport.netty.client.HealthManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
@Component
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {

    /**
     * loadBalance 也得配置注入
     */
    private final LoadBalance loadBalance;

    @Autowired
    public ZkServiceDiscovery(RpcContext rpcContext) {
        loadBalance = LoadBalanceFactory.getLoadBalance(rpcContext.getLoadBalance());
    }

    @Override
    public InetSocketAddress discoverService(String serviceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceAddressList = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        checkAddressList(serviceName,serviceAddressList);
        String [] address = loadBalance.selectAddress(serviceAddressList).split(":");
        return new InetSocketAddress(address[0], Integer.parseInt(address[1]));
    }
    private void checkAddressList(String serviceName,List<String> addressList){
        if (Objects.isNull(addressList) || addressList.isEmpty()){
            throw new ServiceException("not fund service provider : [ "+serviceName+"]");
        }
    }
}