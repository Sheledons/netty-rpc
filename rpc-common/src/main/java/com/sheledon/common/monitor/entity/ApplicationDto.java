package com.sheledon.common.monitor.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {
    private String id;
    private String applicationName;
    private List<RpcServiceDto> serviceList;
    private List<String> type;
    private String time;
    private String host;
}
