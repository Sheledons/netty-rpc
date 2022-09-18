package com.sheledon.common.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 收集监控信息
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollectInfo {
    private ApplicationDto applicationDto;
    private List<LogDto> logDtos;
    private Map<String, AtomicInteger> serviceInvokeMap;
}
