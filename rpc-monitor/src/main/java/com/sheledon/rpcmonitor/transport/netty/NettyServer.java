package com.sheledon.rpcmonitor.transport.netty;

import com.sheledon.common.constants.RpcConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 单独开一个线程启动
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Component
@Slf4j
public class NettyServer {
    @Autowired
    private ServerChannelInitializer serverChannelInitializer;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private final ServerBootstrap serverBootstrap;

    public NettyServer() {
        serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,512)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true);
    }
    public void start(){
        serverBootstrap.childHandler(serverChannelInitializer);
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(InetAddress.getLocalHost().getHostAddress(),
                    RpcConstants.MONITOR_PORT);
            channelFuture.addListener((ChannelFutureListener)f->{
                if (f.isSuccess()){
                    log.info("success start monitor netty server address is {}, port is {}", InetAddress.getLocalHost().getHostAddress() ,RpcConstants.MONITOR_PORT);
                } else{
                   f.cause().printStackTrace();
                   f.channel().close();
               }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
