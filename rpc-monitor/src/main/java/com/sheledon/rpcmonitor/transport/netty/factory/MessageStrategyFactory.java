package com.sheledon.rpcmonitor.transport.netty.factory;

import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.rpcmonitor.transport.netty.strategy.MessageStrategy;
import com.sheledon.rpcmonitor.transport.netty.strategy.RpcHeartBeatRequestStrategy;
import com.sheledon.rpcmonitor.transport.netty.strategy.RpcMonitorCollectStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Component
public class MessageStrategyFactory implements InitializingBean {
    @Autowired
    private RpcMonitorCollectStrategy rpcMonitorCollectStrategy;
    @Autowired
    private RpcHeartBeatRequestStrategy rpcHeartBeatRequestStrategy;

    private final Map<Byte,MessageStrategy> messageStrategyMap = new HashMap<>();
    public MessageStrategyFactory() { }

    public MessageStrategy getStrategy(byte messageType){
        return this.messageStrategyMap.get(messageType);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        messageStrategyMap.put(RpcMessageType.RPC_MONITOR_COLLECT,rpcMonitorCollectStrategy);
        messageStrategyMap.put(RpcMessageType.RPC_HEARTBEAT_REQUEST,rpcHeartBeatRequestStrategy);
    }
}
