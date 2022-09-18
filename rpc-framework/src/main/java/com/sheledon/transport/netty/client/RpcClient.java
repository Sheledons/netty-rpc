package com.sheledon.transport.netty.client;

import com.sheledon.common.constants.RpcConstants;
import com.sheledon.common.exception.FailConnectRemoteException;
import com.sheledon.common.monitor.entity.CollectInfo;
import com.sheledon.common.netty.utils.IDUtils;
import com.sheledon.context.RpcContext;
import com.sheledon.provider.impl.exception.ServiceException;
import com.sheledon.register.ServiceDiscovery;
import com.sheledon.transport.RpcRequestTransport;
import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import com.sheledon.transport.netty.factory.RpcRequestProcessFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:05
 * @Version 1.0
 */
@Slf4j
@Component
public class RpcClient implements RpcRequestTransport {
    private static final int MAX_RETRY_NUMS = 30;
    private final Bootstrap bootstrap;
    @Autowired
    private ServiceDiscovery serviceDiscovery;
    @Autowired
    private ClientChannelInitializer clientChannelInitializer;
    @Autowired
    private RpcContext rpcContext;
    @Autowired
    private HealthManager healthManager;
    /**
     * 锁池
     * key : "ip:port"
     */
    private static final Map<String, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();
    private static final Map<String,Channel> CONNECT_CHANNEL_MAP = new ConcurrentHashMap<>();

    public RpcClient() {
        bootstrap = new Bootstrap();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR,Boolean.TRUE)
                .option(ChannelOption.SO_KEEPALIVE,Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY,Boolean.TRUE);
    }

    @SneakyThrows
    private Channel connect(InetSocketAddress remoteAddress){
        bootstrap.handler(clientChannelInitializer);
        ChannelFuture channelFuture = bootstrap.connect(remoteAddress);
        Promise<Channel> promise = channelFuture.channel().eventLoop().newPromise();
        channelFuture.addListener((ChannelFutureListener)f->{
            if (f.isSuccess()){
                log.info("success connect address : {}",remoteAddress);
                promise.setSuccess(f.channel());
            }else{
                log.error("fail connect : {}",remoteAddress);
                f.cause().printStackTrace();
                promise.setFailure(null);
            }
        });
        return promise.get();
    }
    @SneakyThrows
    @Override
    public CompletableFuture<RpcResponse<Object>> sendRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse<Object>> completableFuture = new CompletableFuture<>();
        RpcRequestProcessFactory.put(rpcRequest.getRequestId(),completableFuture);
        Channel channel = getRpcChannel(rpcRequest.getServiceName());
        byte st = 1;
        RpcMessage message = RpcMessage.builder()
                .messageType(RpcMessageType.RPC_REQUEST)
                .rpcMessageId(IDUtils.getRandomLongId())
                .serializationType(st)
                .body(rpcRequest)
                .build();
        channel.writeAndFlush(message);
        return completableFuture;
    }
    @Override
    public CompletableFuture<RpcResponse<?>> sendCollectInfoToMonitor(CollectInfo collectInfo) {
        CompletableFuture<RpcResponse<?>> completableFuture = new CompletableFuture<>();
        String monitorUrl = rpcContext.getRpcMonitorUrl();
        String addressStr = monitorUrl+":"+RpcConstants.MONITOR_PORT;
        Channel channel = CONNECT_CHANNEL_MAP.get(addressStr);
        if (!isActive(channel)){
            channel = this.connect(new InetSocketAddress(monitorUrl, RpcConstants.MONITOR_PORT));
            if (!isActive(channel)){
                completableFuture.completeExceptionally(new FailConnectRemoteException("fail connect "+addressStr));
                return completableFuture;
            }
            CONNECT_CHANNEL_MAP.put(addressStr,channel);
        }
        byte st = 1;
        RpcMessage message = RpcMessage.builder()
                .messageType(RpcMessageType.RPC_MONITOR_COLLECT)
                .rpcMessageId(IDUtils.getRandomLongId())
                .serializationType(st)
                .body(collectInfo)
                .build();
        channel.writeAndFlush(message).addListener(f->{
            if (f.isSuccess()){
                completableFuture.complete(null);
            }else{
                completableFuture.completeExceptionally(new Exception("fail send collect info"));
            }
        });
        return completableFuture;
    }
    private Channel getRpcChannel(String serviceName){
        for (int i = 0; i < MAX_RETRY_NUMS; i++) {
            InetSocketAddress address = serviceDiscovery.discoverService(serviceName);
            String addressStr = address.toString();
            Channel channel = CONNECT_CHANNEL_MAP.get(addressStr);
            if (Objects.isNull(channel)){
                ReentrantLock lock = LOCK_MAP.computeIfAbsent(addressStr, key->new ReentrantLock());
                lock.lock();
                try {
                    Channel ch = CONNECT_CHANNEL_MAP.get(addressStr);
                    if (isActive(ch)){
                        continue;
                    }
                    CONNECT_CHANNEL_MAP.remove(addressStr);
                    Channel rpcChannel = this.connect(address);
                    if (isActive(rpcChannel)){
                        CONNECT_CHANNEL_MAP.put(addressStr,rpcChannel);
                        LOCK_MAP.remove(addressStr);
                        return rpcChannel;
                    }
                }finally {
                    lock.unlock();
                }
            }else if (healthManager.isGreen(channel)&&isActive(channel)){
                return channel;
            }
        }
        throw new ServiceException("not found active service provider : ["+serviceName+"]");
    }
    private boolean isActive(Channel channel){
        return !Objects.isNull(channel) && channel.isActive();
    }
}
