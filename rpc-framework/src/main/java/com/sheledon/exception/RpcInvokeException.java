package com.sheledon.exception;

/**
 * @author Sheledon
 * @date 2022/3/29
 * @Version 1.0
 */
public class RpcInvokeException extends RuntimeException{
    public RpcInvokeException(){}
    public RpcInvokeException(String message){
        super(message);
    }
}
