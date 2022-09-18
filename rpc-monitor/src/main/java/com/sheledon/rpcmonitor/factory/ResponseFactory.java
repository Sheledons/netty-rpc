package com.sheledon.rpcmonitor.factory;

import com.sheledon.rpcmonitor.common.CodeEnum;
import com.sheledon.rpcmonitor.vo.ResponseVo;

/**
 * @author Sheledon
 * @date 2022/2/19
 * @Version 1.0
 */
public class ResponseFactory {
    public static ResponseVo buildSuccessResponse(Object data){
        return ResponseVo.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }
    public static ResponseVo buildSuccessResponse(Object data,String message){
        return ResponseVo.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(data)
                .message(message)
                .build();
    }
    public static ResponseVo buildFailResponse(String message){
        return ResponseVo.builder()
                .code(CodeEnum.FAIL.getCode())
                .message(message)
                .build();
    }
}
