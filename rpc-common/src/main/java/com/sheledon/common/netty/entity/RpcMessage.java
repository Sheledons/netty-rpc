package com.sheledon.common.netty.entity;
import com.sheledon.common.netty.constants.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * /**
 *  *  ---------------------------------------------------------------------------------------------------------------
 *  *  | magic_number(1)  |  version(1) | contentLength (4) | headLength (4) | messageType (1) | serializationType(1) |
 *  *  ---------------------------------------------------------------------------------------------------------------
 *  *  |                                   requestId (8)                                                              |
 *  *  ----------------------------------------------------------------------------------------------------------------
 *  *  |                                   扩展字段                                                                    |
 *  *  ----------------------------------------------------------------------------------------------------------------
 *  *  |                                   body                                                                       |
 *  *  ----------------------------------------------------------------------------------------------------------------
 * RPC应用层协议
 * @Author sheledon
 * @Date 2021/11/5 上午9:35
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcMessage{
    private byte magicNumber;
    private byte version;
    private int contentLength;
    private final int headLength = RpcConstant.HEAD_LENGTH;
    private byte messageType;
    private byte serializationType;
    private long rpcMessageId;
    private Object body;
}
