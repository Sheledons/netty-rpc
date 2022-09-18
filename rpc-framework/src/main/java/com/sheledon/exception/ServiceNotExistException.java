package com.sheledon.exception;

/**
 * @author Sheledon
 * @date 2021/11/14
 * @Version 1.0
 */
public class ServiceNotExistException extends RuntimeException{
    public ServiceNotExistException() {
    }

    public ServiceNotExistException(String message) {
        super(message);
    }
}
