package com.sheledon.transport.netty.client;

import com.sheledon.common.netty.codec.RpcMessageCodec;
import com.sheledon.transport.netty.client.handler.ClientHandler;
import com.sheledon.transport.netty.client.handler.RpcReadTimeoutHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @Author sheledon
 * @Date 2021/11/5 下午3:37
 * @Version 1.0
 */
@Component
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ClientHandler clientHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new RpcMessageCodec())
                .addLast(new RpcReadTimeoutHandler(40,TimeUnit.SECONDS))
                .addLast(new IdleStateHandler(0,90, 90,TimeUnit.MINUTES))
                .addLast(clientHandler);
    }
}
