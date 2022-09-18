package com.sheledon.rpcmonitor.service.impl;

import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.rpcmonitor.mapper.ApplicationMapper;
import com.sheledon.rpcmonitor.service.RpcApplicationService;
import com.sheledon.rpcmonitor.service.RpcService;
import com.sheledon.rpcmonitor.vo.ApplicationVo;
import com.sheledon.rpcmonitor.vo.RpcServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Service
public class RpcApplicationServiceImpl implements RpcApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private RpcService rpcService;

    @Override
    public void addApplication(ApplicationDto application) {
        applicationMapper.save(application);
    }

    @Override
    public void addApplicationAddress(String addressStr, String applicationName) {
        applicationMapper.addApplicationAddress(addressStr,applicationName);
    }

    @Override
    public void removeApplicationByAddress(String address) {
        applicationMapper.removeApplicationByAddress(address);
    }

    @Override
    public List<ApplicationDto> getApplicationList() {
        return applicationMapper.getList();
    }

    @Override
    public ApplicationVo getApplicationByName(String applicationName) {
        ApplicationDto applicationDto = applicationMapper.getByName(applicationName);
        List<RpcServiceVo> serivceList = rpcService.getRpcServiceByName(applicationName);
        return ApplicationVo.builder()
                .id(applicationDto.getId())
                .applicationName(applicationDto.getApplicationName())
                .type(applicationDto.getType())
                .time(applicationDto.getTime())
                .host(applicationDto.getHost())
                .serviceList(serivceList)
                .build();
    }
}
