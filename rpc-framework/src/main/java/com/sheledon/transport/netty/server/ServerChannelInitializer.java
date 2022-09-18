package com.sheledon.transport.netty.server;

import com.sheledon.common.netty.codec.RpcMessageCodec;
import com.sheledon.transport.netty.server.handler.ServerExceptionHandler;
import com.sheledon.transport.netty.server.handler.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:16
 * @Version 1.0
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private ServerHandler serverHandler;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        处理无法捕捉远程主机强迫关闭一个现有连接这一个异常
        ch.config().setAllowHalfClosure(true);
        ch.pipeline()
                .addLast(new RpcMessageCodec())
                .addLast(serverHandler)
                .addLast(new ServerExceptionHandler());
    }
}
