package com.sheledon.context;

import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import lombok.Builder;
import lombok.Data;

/**
 * monitor收集信息的实体类
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Builder
@Data
public class RpcInvokerContext {
    private String id;
    private RpcRequest request;
    private RpcResponse<?> response;
    private String consumer;
    private String provider;
    private long startTime;
    private long endTime;
}
