package com.sheledon.provider.impl.exception;

/**
 * @author Sheledon
 * @date 2021/11/19
 * @Version 1.0
 */
public class ServiceException extends RuntimeException{
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
