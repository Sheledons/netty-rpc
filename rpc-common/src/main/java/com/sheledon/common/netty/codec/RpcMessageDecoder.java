package com.sheledon.common.netty.codec;
import com.sheledon.common.monitor.entity.CollectInfo;
import com.sheledon.common.netty.constants.RpcConstant;
import com.sheledon.common.netty.serialize.Serializer;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.entity.RpcRequest;
import com.sheledon.common.netty.entity.RpcResponse;
import com.sheledon.common.netty.serialize.protobuf.ProtostuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:31
 * @Version 1.0
 */
@Slf4j
public class RpcMessageDecoder extends ReplayingDecoder<RpcMessage> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte magicNumber = in.readByte();
        checkMagicNumber(magicNumber);
        byte version = in.readByte();
        int contentLength = in.readInt();
        int headLength = in.readInt();
        byte messageType = in.readByte();
        byte serializationType = in.readByte();
        long requestId  = in.readLong();
        RpcMessage rpcMessage = RpcMessage.builder()
                .magicNumber(magicNumber)
                .version(version)
                .contentLength(contentLength)
                .messageType(messageType)
                .serializationType(serializationType)
                .rpcMessageId(requestId)
                .build();
        int bl = contentLength - headLength;
        if (bl>0){
            byte [] bodyBytes = new byte[contentLength-headLength];
            in.readBytes(bodyBytes);
            Serializer instance = SingletonFactory.getInstance(ProtostuffSerializer.class);
            Object body= instance.deserialize(bodyBytes, RPC_MESSAGE_TYPE_MAP.get(messageType));
            rpcMessage.setBody(body);
        }
        out.add(rpcMessage);
    }
    private final static Map<Byte,Class<?>> RPC_MESSAGE_TYPE_MAP = new ConcurrentHashMap<>();
    static {
        RPC_MESSAGE_TYPE_MAP.put(RpcMessageType.RPC_REQUEST, RpcRequest.class);
        RPC_MESSAGE_TYPE_MAP.put(RpcMessageType.RPC_RESPONSE, RpcResponse.class);
        RPC_MESSAGE_TYPE_MAP.put(RpcMessageType.RPC_MONITOR_COLLECT, CollectInfo.class);
    }
    private void checkMagicNumber(byte magicNumber){
        if (RpcConstant.MAGIC_NUMBER != magicNumber){
            throw new UnsupportedOperationException("not allow magicNumber");
        }
    }
}
