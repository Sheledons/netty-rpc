package com.sheledon.transport.netty;

import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.transport.netty.client.HealthManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;


/**
 * 保存通信会话信息
 * @author Sheledon
 * @date 2022/4/7
 */
@Getter
public class RpcSession {
    private final RpcMessage rpcMessage;
    private final ChannelHandlerContext channelContext;

    public RpcSession(RpcMessage rpcMessage, ChannelHandlerContext channelContext) {
        this.rpcMessage = rpcMessage;
        this.channelContext = channelContext;
    }

    public static RpcSession getSession(RpcMessage rpcMessage, ChannelHandlerContext channelContext){
        return new RpcSession(rpcMessage,channelContext);
    }
}
