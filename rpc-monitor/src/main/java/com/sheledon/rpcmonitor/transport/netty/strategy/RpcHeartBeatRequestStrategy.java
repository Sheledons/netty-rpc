package com.sheledon.rpcmonitor.transport.netty.strategy;

import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author Sheledon
 * @date 2022/4/7
 */
@Component
public class RpcHeartBeatRequestStrategy implements MessageStrategy{
    @Override
    public RpcMessage handle(ChannelHandlerContext context, RpcMessage message) {
        byte st = 1;
        System.out.println("心跳处理");
        return RpcMessage.builder()
                .messageType(RpcMessageType.RPC_HEARTBEAT_RESPONSE)
                .serializationType(st)
                .rpcMessageId(message.getRpcMessageId())
                .build();
    }
}
