package com.sheledon.common.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    private long startTime;
    private long endTime;
    private long spendTime;
    private String consumer;
    private String provider;
    private String serviceName;
    private int status;
}
