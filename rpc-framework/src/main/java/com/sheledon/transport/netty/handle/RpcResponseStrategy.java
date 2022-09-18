package com.sheledon.transport.netty.handle;

import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.entity.RpcResponse;
import com.sheledon.transport.netty.RpcSession;
import com.sheledon.transport.netty.factory.RpcRequestProcessFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sheledon
 * @date 2021/11/12
 * @Version 1.0
 */
@Slf4j
public class RpcResponseStrategy implements MessageStrategy{
    @Override
    public RpcMessage handle(RpcSession session) {
        RpcMessage message = session.getRpcMessage();
        RpcRequestProcessFactory.complete((RpcResponse<Object>) message.getBody());
        log.info("rpc response : {}",message.getBody());
        return null;
    }
}
