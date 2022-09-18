package com.sheledon.transport.netty.factory;

import com.sheledon.context.RpcContext;
import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.transport.netty.client.HealthManager;
import com.sheledon.transport.netty.handle.MessageStrategy;
import com.sheledon.transport.netty.handle.RpcHeartBeatRequestStrategy;
import com.sheledon.transport.netty.handle.RpcHeartBeatResponseStrategy;
import com.sheledon.transport.netty.handle.RpcRequestStrategy;
import com.sheledon.transport.netty.handle.RpcResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂
 * @author Sheledon
 * @date 2021/11/12
 * @Version 1.0
 */
@Component
public class MessageStrategyFactory {

    private final Map<Byte, MessageStrategy> strategyMap = new ConcurrentHashMap<>();
    @Autowired
    public MessageStrategyFactory(HealthManager healthManager,RpcContext rpcContext) {
        strategyMap.put(RpcMessageType.RPC_REQUEST, new RpcRequestStrategy(rpcContext));
        strategyMap.put(RpcMessageType.RPC_RESPONSE, new RpcResponseStrategy());
        strategyMap.put(RpcMessageType.RPC_HEARTBEAT_RESPONSE, new RpcHeartBeatResponseStrategy(healthManager));
        strategyMap.put(RpcMessageType.RPC_HEARTBEAT_REQUEST, new RpcHeartBeatRequestStrategy());
    }
    public MessageStrategy getMessageStrategy(byte messageType){
        if (!strategyMap.containsKey(messageType)){
            throw new RuntimeException("not exist this message strategy");
        }
        return strategyMap.get(messageType);
    }
}
