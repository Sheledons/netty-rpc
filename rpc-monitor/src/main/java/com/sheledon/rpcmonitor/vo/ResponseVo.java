package com.sheledon.rpcmonitor.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sheledon
 * @date 2022/2/19
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVo {
    private Integer code;
    private String message;
    private Object data;
}
