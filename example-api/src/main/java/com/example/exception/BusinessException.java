package com.example.exception;

import lombok.Builder;
import lombok.Data;

/**
 * @author Sheledon
 * @date 2021/11/21
 * @Version 1.0
 */
@Builder
public class BusinessException extends RuntimeException{
    private int code;
    private String message;
    private Object data;

    public BusinessException() {
    }

    public BusinessException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
