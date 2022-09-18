package com.sheledon.transport.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:05
 * @Version 1.0
 */
@Slf4j
@Component
public class RpcServer {
    private final ServerBootstrap serverBootstrap;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workGroup;
    @Autowired
    private ServerChannelInitializer serverChannelInitializer;
    public final static int SERVER_PORT = getRandomPort();

    public RpcServer(){
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,512)
                .childOption(ChannelOption.SO_KEEPALIVE,Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY,Boolean.TRUE);
    }
    public void start() throws UnknownHostException{
        try {
            serverBootstrap.childHandler(serverChannelInitializer);
            ChannelFuture channelFuture  = serverBootstrap.bind(InetAddress.getLocalHost().getHostAddress(),SERVER_PORT);
            channelFuture.addListener((ChannelFutureListener) f->{
                if (f.isSuccess()){
                    log.info("netty-rpc server success start,listen port :{}", SERVER_PORT);
                }else{
                    log.error("netty-rpc server fail start,listen port :{}", SERVER_PORT);
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty-rpc server fail start,listen port :{}", SERVER_PORT);
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            log.info("netty-rpc shutdownGracefully");
        }
    }
    @SneakyThrows
    private static int getRandomPort(){
        ServerSocket serverSocket = new ServerSocket(0);
        serverSocket.close();
        return serverSocket.getLocalPort();
    }
}
