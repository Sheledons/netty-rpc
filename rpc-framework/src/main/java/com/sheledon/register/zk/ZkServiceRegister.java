package com.sheledon.register.zk;

import com.sheledon.register.ServiceRegister;
import com.sheledon.register.zk.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public class ZkServiceRegister implements ServiceRegister {

    @Override
    public void registerService(String serviceName, InetSocketAddress address) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        String path = CuratorUtils.ZK_REGISTER_ROOT_PATH+"/"+serviceName+address;
        CuratorUtils.createNode(zkClient,CreateMode.EPHEMERAL,path);
    }

}
