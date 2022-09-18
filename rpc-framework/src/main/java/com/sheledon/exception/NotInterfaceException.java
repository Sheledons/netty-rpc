package com.sheledon.exception;

/**
 * @author Sheledon
 * @date 2021/11/15
 * @Version 1.0
 */
public class NotInterfaceException extends RuntimeException{
    public NotInterfaceException() {
    }

    public NotInterfaceException(String message) {
        super(message);
    }
}
