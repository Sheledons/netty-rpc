package com.sheledon.transport.netty.handle;

import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.transport.netty.RpcSession;

/**
 * @author Sheledon
 * @date 2022/4/7
 */
public class RpcHeartBeatRequestStrategy implements MessageStrategy{
    @Override
    public RpcMessage handle(RpcSession session) {
        RpcMessage message = session.getRpcMessage();
        byte st = 1;
        return RpcMessage.builder()
                .messageType(RpcMessageType.RPC_HEARTBEAT_RESPONSE)
                .serializationType(st)
                .rpcMessageId(message.getRpcMessageId())
                .build();
    }
}
