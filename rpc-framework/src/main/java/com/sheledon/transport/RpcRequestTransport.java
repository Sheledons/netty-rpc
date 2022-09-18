package com.sheledon.transport;


import com.sheledon.common.monitor.entity.CollectInfo;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;

/**
 * 客户端发送请求接口
 */
public interface RpcRequestTransport {
    CompletableFuture<RpcResponse<Object>> sendRequest(RpcRequest rpcRequest);
    CompletableFuture<?> sendCollectInfoToMonitor(CollectInfo collectInfo);
}
