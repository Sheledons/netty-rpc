package com.sheledon.rpcmonitor.transport.netty.handler;

import com.sheledon.rpcmonitor.service.RpcApplicationService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
public class ServerExceptionHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private RpcApplicationService applicationService;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String address = ctx.channel().remoteAddress().toString();
        applicationService.removeApplicationByAddress(address);
        ctx.channel().close();
    }
}
