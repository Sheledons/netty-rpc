package com.sheledon.transport.netty.server.handler;

import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.transport.netty.RpcSession;
import com.sheledon.transport.netty.factory.MessageStrategyFactory;
import com.sheledon.transport.netty.handle.MessageStrategy;
import com.sheledon.transport.netty.handle.RpcHeartBeatRequestStrategy;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:14
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<RpcMessage> {


    @Autowired
    private MessageStrategyFactory messageStrategyFactory;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        RpcSession session = RpcSession.getSession(msg,ctx);
        MessageStrategy strategy = messageStrategyFactory.getMessageStrategy(msg.getMessageType());
        Object result = strategy.handle(session);
        ctx.writeAndFlush(result).addListener((ChannelFutureListener) f->{
            if (!f.isSuccess()){
                log.error("send rpcMessage fail : {}",result);
                f.channel().close();
                AtomicInteger integer = new AtomicInteger(0);
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("channel close , address : {}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }
}
