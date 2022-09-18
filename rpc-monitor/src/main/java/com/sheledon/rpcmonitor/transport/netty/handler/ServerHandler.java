package com.sheledon.rpcmonitor.transport.netty.handler;

import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.rpcmonitor.service.RpcApplicationService;
import com.sheledon.rpcmonitor.transport.netty.factory.MessageStrategyFactory;
import com.sheledon.rpcmonitor.transport.netty.strategy.MessageStrategy;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 心跳处理和统计信息的处理
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    @Autowired
    private MessageStrategyFactory strategyFactory;
    @Autowired
    private RpcApplicationService applicationService;

    public ServerHandler(){ }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        byte messageType = rpcMessage.getMessageType();
        MessageStrategy strategy = strategyFactory.getStrategy(messageType);
        RpcMessage result = strategy.handle(channelHandlerContext,rpcMessage);
        if (!Objects.isNull(result)){
            channelHandlerContext.writeAndFlush(result);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state()== IdleState.READER_IDLE){
                String address = ctx.channel().remoteAddress().toString();
                applicationService.removeApplicationByAddress(address);
            }
        }else if(evt instanceof ChannelInboundHandlerAdapter){
            ctx.channel().close();
        } else{
            ctx.fireUserEventTriggered(evt);
        }
    }
}
