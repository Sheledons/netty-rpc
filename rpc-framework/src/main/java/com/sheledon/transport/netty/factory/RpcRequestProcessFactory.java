package com.sheledon.transport.netty.factory;

import com.sheledon.common.netty.entity.RpcResponse;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC请求工厂
 * @author Sheledon
 * @date 2021/11/12
 * @Version 1.0
 */
public class RpcRequestProcessFactory {
    private static Map<String, CompletableFuture<RpcResponse<Object>>> unprocessedMap = new ConcurrentHashMap<>();
    public static void put(String key, CompletableFuture<RpcResponse<Object>> value) {
        unprocessedMap.put(key,value);
    }
    public static void complete(RpcResponse<Object> rpcResponse){
        String key = rpcResponse.getResponseId();
        CompletableFuture<RpcResponse<Object>> future = unprocessedMap.remove(key);
        if (Objects.isNull(future)){
            throw new IllegalArgumentException();
        }
        future.complete(rpcResponse);
    }
}
