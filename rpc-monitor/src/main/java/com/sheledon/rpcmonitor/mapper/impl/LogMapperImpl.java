package com.sheledon.rpcmonitor.mapper.impl;

import com.sheledon.common.monitor.entity.LogDto;
import com.sheledon.rpcmonitor.mapper.LogMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Sheledon
 * @date 2022/2/20
 * @Version 1.0
 */
@Component
public class LogMapperImpl implements LogMapper {
    private List<LogDto> logDtoList = new LinkedList<>();
    @Override
    public void saveLog(List<LogDto> logDtos) {
        if (Objects.isNull(logDtos)){
            return;
        }
        logDtoList.addAll(logDtos);
    }

    @Override
    public List<LogDto> getLogList() {
        return logDtoList;
    }
}
