package com.sheledon.common.netty.codec;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @author Sheledon
 * @date 2022/3/16
 * @Version 1.0
 */
public class RpcMessageCodec extends CombinedChannelDuplexHandler<RpcMessageDecoder,RpcMessageEncoder> {
    public RpcMessageCodec() {
        super(new RpcMessageDecoder(),new RpcMessageEncoder());
    }
}
