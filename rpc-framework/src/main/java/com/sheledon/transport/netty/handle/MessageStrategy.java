package com.sheledon.transport.netty.handle;

import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.transport.netty.RpcSession;


/**
 * @author Sheledon
 * @date 2021/11/12
 * @Version 1.0
 */
public interface MessageStrategy {
    /**
     * 策略接口，对于不同的消息类型有不同的处理方式
     * @return
     */
     RpcMessage handle(RpcSession session);
}
