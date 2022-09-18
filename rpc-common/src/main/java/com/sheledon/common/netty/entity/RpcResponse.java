package com.sheledon.common.netty.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author sheledon
 * @Date 2021/11/5 上午9:35
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse<T> implements Serializable {
    private String responseId;
    private String provider;
    private Integer code;
    private String message;
    private T data;
}
