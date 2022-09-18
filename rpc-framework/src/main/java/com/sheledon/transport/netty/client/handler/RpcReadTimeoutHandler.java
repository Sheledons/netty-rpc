package com.sheledon.transport.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * 不可以使用原生的ReadTimeoutHandler因为一旦readTimeoutException则关闭通道
 * @author Sheledon
 * @date 2022/4/7
 */
public class RpcReadTimeoutHandler extends ReadTimeoutHandler {
    public RpcReadTimeoutHandler(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    public RpcReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }
    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
    }

}
