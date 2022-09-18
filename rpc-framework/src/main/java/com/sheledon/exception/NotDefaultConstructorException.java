package com.sheledon.exception;

/**
 * @author Sheledon
 * @date 2021/11/15
 * @Version 1.0
 */
public class NotDefaultConstructorException extends RuntimeException{
    public NotDefaultConstructorException() {
    }

    public NotDefaultConstructorException(String message) {
        super(message);
    }
}
