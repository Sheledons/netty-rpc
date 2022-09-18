package com.sheledon.common.netty.codec;

import com.sheledon.common.netty.serialize.Serializer;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.common.netty.constants.RpcConstant;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.common.netty.serialize.hessian.HessianSerializer;
import com.sheledon.common.netty.serialize.kyro.KyroSerializer;
import com.sheledon.common.netty.serialize.protobuf.ProtostuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:32
 * @Version 1.0
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception{
        log.info("encode : {}",msg);
        out.writeByte(RpcConstant.MAGIC_NUMBER);
        out.writeByte(RpcConstant.VERSION);
        int contentLength = RpcConstant.HEAD_LENGTH;
        byte [] bodyBytes = new byte[0];
        if (!Objects.isNull(msg.getBody())){
            Serializer instance = SingletonFactory.getInstance(ProtostuffSerializer.class);
            bodyBytes = instance.serialize(msg.getBody());
        }
        contentLength+=bodyBytes.length;
        out.writeInt(contentLength);
        out.writeInt(RpcConstant.HEAD_LENGTH);
        out.writeByte(msg.getMessageType());
        out.writeByte(msg.getSerializationType());
        out.writeLong(msg.getRpcMessageId());
        if (bodyBytes.length>0){
            out.writeBytes(bodyBytes);
        }
    }

}
