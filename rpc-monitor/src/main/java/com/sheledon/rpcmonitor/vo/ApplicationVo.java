package com.sheledon.rpcmonitor.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/20
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationVo {
    private String id;
    private String applicationName;
    private List<RpcServiceVo> serviceList;
    private List<String> type;
    private String time;
    private String host;
}
