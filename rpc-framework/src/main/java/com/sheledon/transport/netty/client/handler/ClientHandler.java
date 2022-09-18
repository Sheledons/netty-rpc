package com.sheledon.transport.netty.client.handler;

import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.utils.IDUtils;
import com.sheledon.transport.netty.RpcSession;
import com.sheledon.transport.netty.client.HealthManager;
import com.sheledon.transport.netty.factory.MessageStrategyFactory;
import com.sheledon.transport.netty.handle.MessageStrategy;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author sheledon
 * @Date 2021/11/5 下午3:37
 * @Version 1.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    @Autowired
    private HealthManager healthManager;

    @Autowired
    private MessageStrategyFactory messageStrategyFactory;

    private final Map<Channel, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        healthManager.addChannel(ctx.channel());
        ScheduledFuture<?> scheduledFuture = ctx.executor().scheduleAtFixedRate(() -> {
            byte st = 1;
            ctx.writeAndFlush(RpcMessage.builder()
                    .messageType(RpcMessageType.RPC_HEARTBEAT_REQUEST)
                    .serializationType(st)
                    .rpcMessageId(IDUtils.getRandomLongId())
                    .build());
        }, 0, 30, TimeUnit.SECONDS);
        scheduledFutureMap.put(ctx.channel(),scheduledFuture);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                RpcMessage message) throws Exception {
        RpcSession session = RpcSession.getSession(message,ctx);
        MessageStrategy strategy = messageStrategyFactory.getMessageStrategy(message.getMessageType());
        strategy.handle(session);
        ctx.pipeline();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE){
                log.warn("writer idle, close channel : {}", ctx.channel().localAddress());
                ctx.channel().close();
            }
        }else{
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(ctx.channel());
        scheduledFuture.cancel(false);
        log.warn("channel close address : {}",ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException){
            //超时检测
            log.warn("channel readTimeout : {}",ctx.channel().remoteAddress());
            this.healthManager.incrementState(ctx.channel());
        }else{
            cause.printStackTrace();
            ctx.channel().close();
        }
    }
}
