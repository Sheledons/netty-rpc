package com.sheledon.rpcmonitor.mapper.impl;

import com.sheledon.common.constants.RpcConstants;
import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.rpcmonitor.mapper.ApplicationMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sheledon
 * @date 2022/2/20
 * @Version 1.0
 */
@Component
public class ApplicationMapperImpl implements ApplicationMapper {

    private final Map<String, ApplicationDto> applicationDtoMap = new ConcurrentHashMap<>();
    /**
     * 连接异常断开通过channel address 来移除保存的监控信息
     * key : addressStr
     * value : applicationName
     */
    private final Map<String,String> applicationAddressMap =
            new ConcurrentHashMap<>();
    @Override
    public void save(ApplicationDto application) {
        applicationDtoMap.put(application.getApplicationName(), application);
    }

    @Override
    public void addApplicationAddress(String address, String applicationName) {
        applicationAddressMap.put(address,applicationName);
    }

    @Override
    public void removeApplicationByAddress(String address) {
        if (applicationAddressMap.containsKey(address)){
            String apn = applicationAddressMap.get(address);
            applicationAddressMap.remove(apn);
            applicationDtoMap.remove(apn);
        }
    }

    @Override
    public List<ApplicationDto> getList() {
        return new ArrayList<>(applicationDtoMap.values());
    }

    @Override
    public ApplicationDto getByName(String name) {
        return applicationDtoMap.get(name);
    }

    @Override
    public int getServiceProviderCount() {
        AtomicInteger result = new AtomicInteger();
        applicationDtoMap.forEach((key,value)->{
            List<String> type = value.getType();
            if (!Objects.isNull(type) && type.contains(RpcConstants.PROVIDER)){
                result.getAndIncrement();
            }
        });
        return result.get();
    }

    @Override
    public int getServiceConsumerCount() {
        AtomicInteger result = new AtomicInteger();
        applicationDtoMap.forEach((key,value)->{
            List<String> type = value.getType();
            if (!Objects.isNull(type) && type.contains(RpcConstants.CONSUMER)){
                result.getAndIncrement();
            }
        });
        return result.get();
    }
}
