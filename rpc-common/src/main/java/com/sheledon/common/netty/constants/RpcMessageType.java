package com.sheledon.common.netty.constants;
import lombok.Data;

/**
 * @Author sheledon
 * @Date 2021/11/5 下午1:44
 * @Version 1.0
 */
@Data
public class RpcMessageType {
    public static final byte RPC_REQUEST = 1;
    public static final byte RPC_RESPONSE = 2;
    public static final byte RPC_HEARTBEAT_REQUEST= 3;
    public static final byte RPC_HEARTBEAT_RESPONSE = 4;
    public static final byte RPC_MONITOR_COLLECT = 5;
}
