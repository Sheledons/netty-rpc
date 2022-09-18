package com.sheledon.common.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceDto {
    private String group;
    private String version;
    private String serviceName;
}
