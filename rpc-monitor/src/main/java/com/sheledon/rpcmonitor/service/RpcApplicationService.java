package com.sheledon.rpcmonitor.service;

import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.rpcmonitor.vo.ApplicationVo;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
public interface RpcApplicationService {
    void addApplication(ApplicationDto application);
    void addApplicationAddress(String address,String applicationName);
    void removeApplicationByAddress(String address);
    List<ApplicationDto> getApplicationList();
    ApplicationVo getApplicationByName(String applicationName);
}
