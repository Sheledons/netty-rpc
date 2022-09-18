package com.sheledon.monitor;
import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.common.monitor.entity.CollectInfo;
import com.sheledon.common.monitor.entity.LogDto;
import com.sheledon.common.monitor.entity.RpcServiceDto;
import com.sheledon.common.constants.RpcConstants;
import com.sheledon.context.RpcContext;
import com.sheledon.common.netty.utils.IDUtils;
import com.sheledon.config.RpcServiceConfig;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.provider.ServiceProvider;
import com.sheledon.provider.impl.ZkServiceProviderImpl;
import com.sheledon.transport.RpcRequestTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 负责保存监控信息，同时向监控中心发送这些信息
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */
@Component
@Slf4j
public class RpcMonitor implements Runnable {

    private final ConcurrentHashMap<String, RpcServiceConfig> serviceConfigMap ;
    private final Map<String, AtomicInteger> serviceInvokeCount ;
    private final List<LogDto> logDtoList = Collections.synchronizedList(new LinkedList<>());
    @Autowired
    private RpcRequestTransport rpcRequestTransport;
    @Autowired
    private RpcContext rpcContext;
    private ApplicationDto applicationDto;

    @SneakyThrows
    public RpcMonitor() {
        ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        Field serviceConfigMapField = serviceProvider.getClass().getDeclaredField("SERVICE_CONFIG_MAP");
        serviceConfigMapField.setAccessible(true);
        serviceConfigMap = (ConcurrentHashMap<String, RpcServiceConfig>) serviceConfigMapField.get(serviceProvider);
        serviceInvokeCount = new ConcurrentHashMap<>();
    }
    public void incrementServiceInvokeCount(String serviceName){
        if (serviceConfigMap.containsKey(serviceName)){
            AtomicInteger atomicInteger = serviceInvokeCount.computeIfAbsent(serviceName, key -> new AtomicInteger(0));
            atomicInteger.incrementAndGet();
        }

    }
    public void recordLog(LogDto log){
        logDtoList.add(log);
    }
    private List<RpcServiceDto> transferRpcServiceConfigMap(){
        return serviceConfigMap.values().stream().map(serviceConfig -> {
            return RpcServiceDto.builder()
                    .serviceName(serviceConfig.getRpcServiceName())
                    .group(serviceConfig.getGroup())
                    .version(serviceConfig.getVersion())
                    .build();
        }).collect(Collectors.toList());
    }
    @Override
    public void run() {
        //定时向monitor发送监控数据
        if (Objects.isNull(this.applicationDto)){
            this.applicationDto = this.buildApplication();
        }
        CollectInfo collectInfo = CollectInfo.builder()
                .applicationDto(applicationDto)
                .serviceInvokeMap(serviceInvokeCount)
                .logDtos(new ArrayList<>(logDtoList))
                .build();
        CompletableFuture<?> completableFuture = rpcRequestTransport.sendCollectInfoToMonitor(collectInfo);
        completableFuture.whenComplete((s,e)->{
            if (Objects.isNull(e)){
                clear();
            }
        });
    }
    @SneakyThrows
    private ApplicationDto buildApplication(){
        List<String> typeList = new ArrayList<>(2);
        if(rpcContext.isRpcServer()){
            typeList.add(RpcConstants.PROVIDER);
        }
        if (rpcContext.isRpcClient()){
            typeList.add(RpcConstants.CONSUMER);
        }
        return ApplicationDto.builder()
                .id(IDUtils.getRandomStringId())
                .applicationName(rpcContext.getRpcApplicationName())
                .time(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .serviceList(transferRpcServiceConfigMap())
                .host(InetAddress.getLocalHost().getHostAddress())
                .type(typeList)
                .build();
    }
    private void clear(){
        logDtoList.clear();
    }
}
