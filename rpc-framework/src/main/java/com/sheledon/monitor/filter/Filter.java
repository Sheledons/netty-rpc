package com.sheledon.monitor.filter;

import com.sheledon.context.RpcContext;
import com.sheledon.context.RpcInvokerContext;
import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */
public interface Filter {
    default void beforeInvoke(RpcInvokerContext context){}
    default void afterInvoke(RpcInvokerContext context){}
}
