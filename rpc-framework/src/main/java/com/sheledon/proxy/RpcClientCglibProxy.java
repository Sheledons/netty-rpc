package com.sheledon.proxy;

import com.sheledon.common.enums.RpcStateEnum;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import com.sheledon.common.netty.utils.IDUtils;
import com.sheledon.config.RpcServiceConfig;
import com.sheledon.context.RpcContext;
import com.sheledon.context.RpcInvokerContext;
import com.sheledon.exception.RpcInvokeException;
import com.sheledon.monitor.filter.Filter;
import com.sheledon.spring.processor.SpringBeanProcessor;
import com.sheledon.transport.RpcRequestTransport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 基于CglibProxy实现的动态代理
 * @author Sheledon
 * @date 2022/4/25
 */
public class RpcClientCglibProxy implements MethodInterceptor {

    private final RpcRequestTransport rpcRequestTransport;
    private final RpcServiceConfig serviceConfig;
    private final RpcContext rpcContext;

    public RpcClientCglibProxy(RpcRequestTransport rpcRequestTransport,
                               RpcServiceConfig serviceConfig,
                               RpcContext rpcContext) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.serviceConfig = serviceConfig;
        this.rpcContext = rpcContext;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
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
            return new RpcInvokeException(rpcResponse.getMessage());
        }
        return rpcResponse.getData();
    }
    private String getInterfaceName(Method method){
        Class<?> interfaces = method.getDeclaringClass();
        return interfaces.getName();
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
    public Object getProxy(Class<?> clazz){
        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(clazz);
        // 设置方法拦截器
        enhancer.setCallback(this);
        // 创建代理类
        return enhancer.create();
    }
}
