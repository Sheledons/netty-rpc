package com.sheledon.common.exception;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public class SerializeException extends RuntimeException{
    public SerializeException() {
    }

    public SerializeException(String message) {
        super(message);
    }
}