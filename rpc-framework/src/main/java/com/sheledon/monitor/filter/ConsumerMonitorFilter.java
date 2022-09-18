package com.sheledon.monitor.filter;

import com.sheledon.annotation.RpcFilter;
import com.sheledon.context.RpcContext;
import com.sheledon.context.RpcInvokerContext;
import com.sheledon.common.constants.RpcConstants;
import com.sheledon.common.monitor.entity.LogDto;
import com.sheledon.monitor.RpcMonitor;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 客户端监控过滤类，负责向monitor上传监控数据
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */
@RpcFilter(target = RpcConstants.CONSUMER)
public class ConsumerMonitorFilter implements Filter {

    @Autowired
    private RpcContext rpcContext;

    @Autowired
    private RpcMonitor rpcMonitor;
    @Override
    public void beforeInvoke(RpcInvokerContext context) {
        context.setStartTime(System.currentTimeMillis());
    }

    @Override
    public void afterInvoke(RpcInvokerContext rpcInvokerContext) {
        long startTime = rpcInvokerContext.getStartTime();
        long endTime = System.currentTimeMillis();
        RpcRequest rpcRequest = rpcInvokerContext.getRequest();
        RpcResponse<?> rpcResponse = rpcInvokerContext.getResponse();
        LogDto logDto = LogDto.builder()
                .startTime(startTime)
                .endTime(endTime)
                .spendTime(endTime - startTime)
                .serviceName(rpcRequest.getServiceName())
                .provider(rpcResponse.getProvider())
                .consumer(rpcContext.getRpcApplicationName())
                .status(rpcResponse.getCode())
                .build();
       rpcMonitor.recordLog(logDto);
    }
}
