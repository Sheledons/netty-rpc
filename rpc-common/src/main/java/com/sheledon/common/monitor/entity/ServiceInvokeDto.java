package com.sheledon.common.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sheledon
 * @date 2022/2/19
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInvokeDto {
    private String applicationName;
    Map<String, AtomicInteger> serviceInvokeCount;
}
