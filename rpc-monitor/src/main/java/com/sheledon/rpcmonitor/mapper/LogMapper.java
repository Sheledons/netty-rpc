package com.sheledon.rpcmonitor.mapper;

import com.sheledon.common.monitor.entity.LogDto;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/20
 * @Version 1.0
 */
public interface LogMapper {
    void saveLog(List<LogDto> logDtos);
    List<LogDto> getLogList();
}
