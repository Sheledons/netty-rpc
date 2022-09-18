package com.sheledon.rpcmonitor.spring;

import com.sheledon.rpcmonitor.transport.netty.NettyServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Component
@Order(100000)
public class NettyServerRunner implements InitializingBean {
    @Autowired
    private NettyServer nettyServer;
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(()->{
            nettyServer.start();
        }).start();
    }
}
