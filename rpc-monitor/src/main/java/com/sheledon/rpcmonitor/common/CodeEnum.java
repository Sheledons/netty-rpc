package com.sheledon.rpcmonitor.common;

/**
 * @author Sheledon
 * @date 2022/2/19
 * @Version 1.0
 */
public enum CodeEnum {
    FAIL(500),
    SUCCESS(200);
    private int code;

    CodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
