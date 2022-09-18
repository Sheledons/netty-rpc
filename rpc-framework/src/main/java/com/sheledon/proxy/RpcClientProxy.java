package com.sheledon.proxy;

import com.sheledon.common.enums.RpcStateEnum;
import com.sheledon.context.RpcContext;
import com.sheledon.context.RpcInvokerContext;
import com.sheledon.exception.RpcInvokeException;
import com.sheledon.monitor.filter.Filter;
import com.sheledon.spring.processor.SpringBeanProcessor;
import com.sheledon.common.netty.utils.IDUtils;
import com.sheledon.config.RpcServiceConfig;
import com.sheledon.transport.RpcRequestTransport;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 基于JDK动态代理实现的客户端代理类
 * @author sheledon
 * @Date 2021/11/5 下午3:37
 * @Version 1.0
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private final RpcRequestTransport rpcRequestTransport;
    private final RpcServiceConfig serviceConfig;
    private final RpcContext rpcContext;
    public RpcClientProxy(RpcRequestTransport rpcRequestTransport,
                          RpcServiceConfig serviceConfig, RpcContext rpcContext) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.serviceConfig = serviceConfig;
        this.rpcContext = rpcContext;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .version(serviceConfig.getVersion())
                .group(serviceConfig.getGroup())
                .requestId(IDUtils.getRandomStringId())
                .interfaceName(getInterfaceName(method))
                .methodName(method.getName())
                .parameters(args)
                .parametersType(method.getParameterTypes())
                .build();
        RpcInvokerContext rpcInvokerContext = RpcInvokerContext.builder()
                .id(rpcRequest.getRequestId())
                .request(rpcRequest)
                .consumer(rpcContext.getRpcApplicationName())
                .build();
        //过滤器前置处理
        beforeInvoke(rpcInvokerContext);
        CompletableFuture<RpcResponse<Object>> future = rpcRequestTransport.sendRequest(rpcRequest);
        RpcResponse<Object> rpcResponse = future.get();
        rpcInvokerContext.setResponse(rpcResponse);
        rpcInvokerContext.setProvider(rpcResponse.getProvider());
        //过滤器后置处理
        afterInvoke(rpcInvokerContext);
        if (rpcResponse.getCode()!= RpcStateEnum.SUCCESS.code()){
            throw new RpcInvokeException(rpcResponse.getMessage());
        }
        return rpcResponse.getData();
    }

    private String getInterfaceName(Method method){
        Class<?> interfaces = method.getDeclaringClass();
        return interfaces.getName();
    }
    public <T> T getProxy(Class<T> interfaces){
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader()
                ,new Class[]{interfaces},this);
    }
    private void beforeInvoke(RpcInvokerContext invokerContext){
        List<Filter> filterList = SpringBeanProcessor.getConsumerFilterList();
        filterList.forEach(filter->{
            filter.beforeInvoke(invokerContext);
        });
    }
    private void afterInvoke(RpcInvokerContext invokerContext){
        List<Filter> filterList = SpringBeanProcessor.getConsumerFilterList();
        filterList.forEach(filter->{
            filter.afterInvoke(invokerContext);
        });
    }
}
