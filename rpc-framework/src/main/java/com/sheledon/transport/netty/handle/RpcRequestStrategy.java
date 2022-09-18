package com.sheledon.transport.netty.handle;

import com.sheledon.context.RpcContext;
import com.sheledon.context.RpcInvokerContext;
import com.sheledon.common.netty.factory.RpcResponseFactory;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.monitor.filter.Filter;
import com.sheledon.provider.ServiceProvider;
import com.sheledon.provider.impl.ZkServiceProviderImpl;
import com.sheledon.spring.processor.SpringBeanProcessor;
import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import com.sheledon.transport.netty.RpcSession;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @author Sheledon
 * @date 2021/11/12
 * @Version 1.0
 */
@Slf4j
public class RpcRequestStrategy implements MessageStrategy{

    private final ServiceProvider serviceProvider;
    private final RpcContext rpcContext;

    public RpcRequestStrategy(RpcContext rpcContext) {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        this.rpcContext = rpcContext;
    }

    @Override
    public RpcMessage handle(RpcSession session) {
        RpcMessage message = session.getRpcMessage();
        log.info("get rpc request : {}",message);
        RpcRequest request = (RpcRequest) message.getBody();
        String serviceName = request.getServiceName();
        Object service = serviceProvider.getService(serviceName);
        Class<?> clazz = service.getClass();
        RpcResponse<?> rpcResponse;
        RpcInvokerContext rpcInvokerContext = this.getRpcInvokerContext(request);
        try {
            Method method = clazz.getMethod(request.getMethodName(),
                                            request.getParametersType());
            beforeInvoke(rpcInvokerContext);
            log.info("invoke method : {}",request);
            Object result = method.invoke(service, request.getParameters());
            rpcResponse = RpcResponseFactory.buildSuccessResponse(request.getRequestId(), result);
        } catch (InvocationTargetException e) {
            log.error("invoke exception: []",e);
            rpcResponse = RpcResponseFactory.buildFailResponse(request.getRequestId(), e.getTargetException());
        } catch (Exception e) {
            log.error("invoke exception: []",e);
            rpcResponse = RpcResponseFactory.buildFailResponse(request.getRequestId(), e);
        }
        rpcResponse.setProvider(rpcContext.getRpcApplicationName());
        rpcInvokerContext.setResponse(rpcResponse);
        afterInvoke(rpcInvokerContext);
        byte st = 1;
        return RpcMessage.builder()
                .body(rpcResponse)
                .serializationType(st)
                .messageType(RpcMessageType.RPC_RESPONSE)
                .build();
    }
    private RpcInvokerContext getRpcInvokerContext(RpcRequest request){
        return RpcInvokerContext.builder()
                .startTime(System.currentTimeMillis())
                .request(request)
                .consumer(request.getApplicationName())
                .build();
    }
    private void beforeInvoke(RpcInvokerContext rpcInvokerContext){
        List<Filter> filterList = SpringBeanProcessor.getProviderFilterList();
        filterList.forEach(filter->{
            filter.beforeInvoke(rpcInvokerContext);
        });
    }
    private void afterInvoke(RpcInvokerContext rpcInvokerContext){
        List<Filter> filterList = SpringBeanProcessor.getProviderFilterList();
        filterList.forEach(filter->{
            filter.afterInvoke(rpcInvokerContext);
        });
    }
}
