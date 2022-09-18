package com.sheledon.rpcmonitor.mapper;

import com.sheledon.common.monitor.entity.ApplicationDto;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/20
 * @Version 1.0
 */
public interface ApplicationMapper {
    void save(ApplicationDto applicationDto);
    void addApplicationAddress(String address,String applicationName);
    void removeApplicationByAddress(String address);
    List<ApplicationDto> getList();
    ApplicationDto getByName(String name);
    int getServiceProviderCount();
    int getServiceConsumerCount();
}
