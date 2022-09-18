package com.sheledon.rpcmonitor.transport.netty;

import com.sheledon.common.netty.codec.RpcMessageDecoder;
import com.sheledon.common.netty.codec.RpcMessageEncoder;
import com.sheledon.rpcmonitor.transport.netty.handler.ServerExceptionHandler;
import com.sheledon.rpcmonitor.transport.netty.handler.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServerHandler serverHandler;
    @Autowired
    private ServerExceptionHandler serverExceptionHandler;

    private final static int READER_IDLE_TIME = 30;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.config().setAllowHalfClosure(true);
        socketChannel.pipeline()
                .addLast(new RpcMessageEncoder())
                .addLast(new RpcMessageDecoder())
                .addLast(new IdleStateHandler(READER_IDLE_TIME,0,0, TimeUnit.SECONDS))
                .addLast(serverHandler)
                .addLast(serverExceptionHandler);
    }
}
