package com.sheledon.common.enums;

/**
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
public enum RpcStateEnum {
    SUCCESS(2000),
    FAIL(5000);
    int code;
    RpcStateEnum(int code) {
        this.code = code;
    }
    public int code() {
        return code;
    }
}
