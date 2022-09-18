package com.sheledon.rpcmonitor.transport.netty.strategy;

import com.sheledon.common.netty.entity.RpcMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
public interface MessageStrategy {
    RpcMessage handle(ChannelHandlerContext context, RpcMessage message);
}
