package com.sheledon.rpcmonitor.service;

import com.sheledon.common.monitor.entity.LogDto;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
public interface RpcLogService {
    void saveLog(List<LogDto> logDtos);
    List<LogDto> getLogList();
}
