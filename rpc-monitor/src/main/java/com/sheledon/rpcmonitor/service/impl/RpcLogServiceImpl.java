package com.sheledon.rpcmonitor.service.impl;

import com.sheledon.common.monitor.entity.LogDto;
import com.sheledon.rpcmonitor.mapper.LogMapper;
import com.sheledon.rpcmonitor.service.RpcLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Service
public class RpcLogServiceImpl implements RpcLogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public void saveLog(List<LogDto> logDtos) {
        logMapper.saveLog(logDtos);
    }

    @Override
    public List<LogDto> getLogList() {
        return logMapper.getLogList();
    }
}
