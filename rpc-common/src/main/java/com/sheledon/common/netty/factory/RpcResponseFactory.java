package com.sheledon.common.netty.factory;

import com.sheledon.common.netty.utils.PropertiesFileUtil;
import com.sheledon.common.enums.RpcConfigEnum;
import com.sheledon.common.enums.RpcStateEnum;
import com.sheledon.common.netty.entity.RpcResponse;

/**
 * 构建 RpcResponse的工厂
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
public class RpcResponseFactory {


    public static RpcResponse buildSuccessResponse(String responseId){
        return RpcResponse.builder()
                .responseId(responseId)
                .code(RpcStateEnum.SUCCESS.code())
                .build();
    }
    public static <T> RpcResponse<T> buildSuccessResponse(String responseId,
                                                          T data){
        return RpcResponse.<T>builder()
                .responseId(responseId)
                .code(RpcStateEnum.SUCCESS.code())
                .data(data)
                .build();
    }
    public static  RpcResponse buildFailResponse(String responseId,
                                                       Throwable throwable){
        return RpcResponse.builder()
                .responseId(responseId)
                .code(RpcStateEnum.FAIL.code())
                .message(throwable.getMessage())
                .build();
    }
}
