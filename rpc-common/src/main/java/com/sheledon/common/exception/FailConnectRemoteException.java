package com.sheledon.common.exception;

/**
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
public class FailConnectRemoteException extends RuntimeException{
    public FailConnectRemoteException(String message) {
        super(message);
    }
}
